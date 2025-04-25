package moody.ui.screen
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import moody.util.ViewModelFactory
//
//@Composable
//fun RecycleBinScreen(navController: NavHostController) {
//    val context = LocalContext.current
//    val factory = ViewModelFactory(context)
//    val viewModel: DetailViewModel = viewModel(factory = factory)
//    val recycleBinEntries by viewModel.getRecycle().collectAsState(initial = emptyList())
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text(text = "Recycle Bin", style = MaterialTheme.typography.titleLarge)
//
//        if (recycleBinEntries.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "No entries in recycle bin")
//            }
//        } else {
//            LazyColumn {
//                items(recycleBinEntries) { entry ->
//                    RecycleBinItem(entry, onRestore = {
//                        viewModel.restoreEntry(entry)
//                    }, onDelete = {
//                        viewModel.deleteFromRecycleBin(entry)
//                    })
//                }
//            }
//        }
//    }
//}