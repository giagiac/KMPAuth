package it.hypernext.modacenter.fidelity.navigation

const val BOOK_ID_ARG = "bookId"

sealed class Screen(val route: String) {
    data object Login : Screen(route = "login_screen")
    data object Home : Screen(route = "home_screen")
    data object Details : Screen(route = "details_screen/{$BOOK_ID_ARG}") {
        fun passId(id: Int) = "details_screen/$id"
    }

    data object Manage : Screen(route = "manage_screen/{$BOOK_ID_ARG}") {
        fun passId(id: Int = -1) = "manage_screen/$id"
    }
}