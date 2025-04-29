package moody.navigation

import moody.ui.screen.KEY_ID_HARIAN

sealed class Screen(val route: String){
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object Moody: Screen("moodyScreen")
    data object FormBaru: Screen("detailMoodyScreen")
    data object FormUbah: Screen("detailMoodyScreen/{$KEY_ID_HARIAN}"){
        fun withId(id: Long) = "detailMoodyScreen/$id"
    }
    data object Recycle: Screen("recycleScreen")
}