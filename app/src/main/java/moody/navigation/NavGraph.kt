package moody.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import moody.ui.screen.AboutScreen
import moody.ui.screen.DetailMoodyScreen
import moody.ui.screen.KEY_ID_HARIAN
import moody.ui.screen.MainScreen
import moody.ui.screen.MoodyScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            MainScreen(navController)
        }
        composable(route = Screen.About.route){
            AboutScreen(navController)
        }
        composable(route = Screen.Moody.route){
            MoodyScreen(navController)
        }
        composable(route = Screen.FormBaru.route){
            DetailMoodyScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_HARIAN) {type = NavType.LongType}
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_HARIAN)
            DetailMoodyScreen(navController, id)
        }
    }
}


