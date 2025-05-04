package moody.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.vani0066.moody.R
import moody.navigation.Screen
import moody.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleScreen(navController: NavHostController) {
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
                    Text(text = stringResource(R.string.recycle))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        RecycleContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun RecycleContent(modifier: Modifier = Modifier ) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    val filteredData = data.filter { it.isDeleted }

    var showDialog by remember { mutableStateOf(false) }
    var id by remember { mutableLongStateOf(0L) }


    if (filteredData.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            items(filteredData) {
                ListItem(harian = it) {
                    id = it.id
                    showDialog = true
                }
            }
        }
        if (showDialog)
            AlertDialog(
                text = { Text(text = stringResource(R.string.tanya_recycle)) },
                confirmButton = {
                    Row {
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.undoDelete(id)
                        }) {
                            Text(text = stringResource(R.string.tombol_undo))
                        }
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.deletePermanent(id)
                        }) {
                            Text(text = stringResource(R.string.tombol_hapus))
                        }

                    }
                },
                dismissButton = {
                    TextButton(onClick = {showDialog = false }) {
                        Text(text = stringResource(R.string.tombol_batal))
                    }
                },
                onDismissRequest = { showDialog = false}
            )
    }
}
