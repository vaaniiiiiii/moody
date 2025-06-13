package moody.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.vani0066.moody.BuildConfig
import com.vani0066.moody.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moody.model.Gambar
import moody.model.User
import moody.navigation.Screen
import moody.network.ApiStatus
import moody.network.UserDataStore
import moody.ui.theme.MoodyTheme
import moody.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GambarScreen (navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(context))
    val errorMessage by viewModel.erroMassage

    var showDialog by remember { mutableStateOf(false) }
    var showDailyDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    var seletedDaily by remember { mutableStateOf<Gambar?>(null) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showDailyDialog = true
    }
    val launcherEdit = rememberLauncherForActivityResult(CropImageContract()) { result ->
                if (result.isSuccessful) {
                    result.uriContent?.let { uri ->
                        selectedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        }
                    }
                } else {
                    Log.e("CropError", "Crop gagal: ${result.error}")
                }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.Gambar))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()){
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        }
                        else{
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),

                            contentDescription = stringResource(R.string.profil),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.Home.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = stringResource(R.string.Main),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = false,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_daily)
                )
            }
        }
    ) { innerPadding ->
        GambarContent(viewModel, user.email, Modifier.padding(innerPadding),
            onDelete = {Gambar ->
            seletedDaily = Gambar
            showDeleteDialog = true
        },
            onEdit = {Gambar ->
                seletedDaily = Gambar
                showEditDialog = true
            })
        if (showDialog){
            ProfilDialog(
                user = user,
                onDismissRequest = {showDialog = false}) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }
        if (showDailyDialog){
            DailyDialog(
                bitmap = bitmap,
                onDismissRequest = {showDailyDialog = false},
                onImageEdit = {
                    val option = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(option)
                }
            ) { title, mood ->
                viewModel.saveData(user.email, title, mood, bitmap!!)
                showDailyDialog = false
            }
        }
        if (showEditDialog && seletedDaily != null) {
            Log.d("EditDialog", "Menampilkan dialog edit Daily: ${seletedDaily!!.title}")
            Log.d("EditDialog", "imageUlr diterima: ${seletedDaily!!.imageUrl}")
            Log.d("EditDialog", "mood: ${seletedDaily!!.mood}, habitat: ${seletedDaily!!.mood}")
            DailyDialog (
                bitmap = selectedBitmap,
                imageUrl = seletedDaily!!.imageUrl.replace("http", "https"),
                titleInitial = seletedDaily!!.title,
                moodInitial = seletedDaily!!.mood,
                onDismissRequest = { showEditDialog = false },
                onImageEdit =   {
                    val option = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcherEdit.launch(option)
                },
                onConfirmation = { title, mood ->
                    viewModel.updateData(
                        userId = user.email,
                        id = seletedDaily!!.id,
                        title = title,
                        mood = mood,
                        selectedBitmap
                    )
                    selectedBitmap = null
                    showEditDialog = false
                }
            )
        }

        if (showDeleteDialog) {
            DialogHapus(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    seletedDaily?.let { viewModel.deleteData(user.email, it.id) }
                    showDeleteDialog = false
                }
            )
        }

        if (errorMessage != null){
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun GambarContent (viewModel: MainViewModel,userId: String, modifier: Modifier = Modifier, onDelete: (Gambar) -> Unit, onEdit: (Gambar) -> Unit){

    val dataDaily by viewModel.dataDaily
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status){
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize().padding(4.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(dataDaily) {daily ->
                    ListItem(
                        daily = daily,
                        onDelete = {onDelete(daily) },
                        onEdit = {onEdit(daily)}

                    )
                }
            }
        }
        ApiStatus.FAILED -> {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId)},
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Composable
fun ListItem(daily: Gambar, onDelete: () -> Unit, onEdit: () -> Unit) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val isLoggiedIn = user.email.isNotEmpty()
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("userId", daily.userId).apply()
    val userId = sharedPreferences.getString("userId", "") ?: ""

    Box(
        modifier = Modifier.padding(4.dp).border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(daily.imageUrl.replace("http", "https"))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.gambar, daily.title),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding()
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp),
        ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = daily.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = daily.mood,
                    fontStyle = FontStyle.Normal,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            if (isLoggiedIn && userId == user.email) {
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.hapus),
                        tint = Color.White
                    )
                }
                IconButton(onClick = { onEdit() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        tint = Color.White
                    )

                }
            }
        }
    }
}
}

private suspend fun signIn(context: Context, dataStore: UserDataStore){
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore )
    }catch (e: GetCredentialException){
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
){
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        }catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }else{
        Log.e("SIGN-In", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore){
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    }catch (e: ClearCredentialException){
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
        MediaStore.Images.Media.getBitmap(resolver, uri)
    }else{
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GambarScreenPreview() {
    MoodyTheme {
        GambarScreen(rememberNavController())
    }
}