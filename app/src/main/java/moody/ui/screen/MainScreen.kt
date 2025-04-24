package moody.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vani0066.moody.R
import moody.ui.theme.MoodyTheme
import moody.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,

                    ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
//    var tambahCatatan by rememberSaveable { mutableStateOf("") }
    var persenanMood by rememberSaveable { mutableStateOf("") }
    var hasilKegiatan by rememberSaveable { mutableStateOf("") }


    val radioOptions = listOf(
        stringResource(id = R.string.sport),
        stringResource(id = R.string.relaxed)
    )
    var mood by rememberSaveable { mutableStateOf(radioOptions[0]) }
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.intro_moody),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
//        OutlinedTextField(
//            value = tambahCatatan,
//            onValueChange = { tambahCatatan = it },
//            label = { Text(text = stringResource((R.string.tambah_catatan))) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
        OutlinedTextField(
            value = persenanMood,
            onValueChange = { persenanMood = it },
            label = { Text(text = stringResource((R.string.persenan_mood))) },
            trailingIcon = { Text(text = "%") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                MoodOption(
                    label = text,
                    isSelected = mood == text,
                    modifier = Modifier
                        .selectable(
                            selected = mood == text,
                            onClick = { mood = text },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }

        val isInputValid = persenanMood.isNotBlank() &&
                persenanMood.toIntOrNull() in 0..100 &&
                mood.isNotBlank()
        Button(
            onClick = {

                val moodValue = persenanMood.toIntOrNull()
                hasilKegiatan = if (moodValue != null && moodValue in 0..100) {
                    if (moodValue >= 60 && mood == radioOptions[0]) {
                        context.getString(R.string.olahraga_rumah)
                    } else if (moodValue >= 60 && mood == radioOptions[1]) {
                        context.getString(R.string.nonton_film)
                    } else if (moodValue < 60 && mood == radioOptions[0]) {
                        context.getString(R.string.badminton)
                    } else {
                        context.getString(R.string.pantai)
                    }
                } else {
                    context.getString(R.string.pesan_error)
                }
            },
            enabled = isInputValid,
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),

            ) {
            Text(text = stringResource(R.string.cek_kegiatan))
        }
        if (hasilKegiatan.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = hasilKegiatan,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                    Button(
                        onClick = {
                            val moody = persenanMood.toIntOrNull() ?: 0
                            val kategori = if (mood >= 60.toString()) R.string.sport else R.string.relaxed
                            val saran = hasilKegiatan

                            val shareText = context.getString(
                                R.string.bagikan_template,
                                moody,
                                context.getString(kategori).uppercase(),
                                saran
                            )
                            shareData(context, shareText)
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.bagikan))
                    }
                }
            }
        }
    }

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}

@Composable
fun MoodOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    MoodyTheme {
        MainScreen(rememberNavController())
    }
}