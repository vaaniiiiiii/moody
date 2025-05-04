package moody.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vani0066.moody.R
import moody.navigation.Screen
import moody.ui.theme.MoodyTheme
import moody.util.ViewModelFactory

const val KEY_ID_HARIAN = "idHarian"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMoodyScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var harian by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(R.string.senang),
        stringResource(R.string.sedih)
    )

    var moodOption by remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(Unit) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getHarian(id) ?: return@LaunchedEffect
        judul = data.judul
        harian = data.harian
        moodOption = data.mood
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
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        if (judul == "" || harian == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(judul, harian, moodOption)
                        } else {
                            viewModel.update(id, judul, harian, moodOption)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false },
                            onConfirmation = {
                                showDialog = false
                                viewModel.softDelete(id)
                                navController.navigate(Screen.Moody.route)
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        FormCatatan(
            title = judul,
            onTitleChange = { judul = it },
            desc = harian,
            onDescChange = { harian = it },
            mood = moodOption,
            onMoodChange = { moodOption = it },
            radioOption = radioOptions,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormCatatan(
    title: String, onTitleChange: (String) -> Unit,
    desc: String, onDescChange: (String) -> Unit,
    mood: String, onMoodChange: (String) -> Unit,
    radioOption: List<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = desc,
            onValueChange = { onDescChange(it) },
            label = { Text(text = stringResource(R.string.isi_catatan)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOption.forEach { moodLabel ->
                MoodOption(
                    label = moodLabel,
                    isSelected = mood == moodLabel,
                    onClick = { onMoodChange(moodLabel) }
                )
            }
        }
    }
}

@Composable
fun MoodOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
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
fun DetailMoodyScreenPreview() {
    MoodyTheme {
        DetailMoodyScreen(rememberNavController())
    }
}