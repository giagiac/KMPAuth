package it.hypernext.modacenter.fidelity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.hypernext.modacenter.fidelity.presentation.screen.details.DetailsScreen
import it.hypernext.modacenter.fidelity.presentation.screen.home.HomeScreen
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginScreen
import it.hypernext.modacenter.fidelity.presentation.screen.manage.ManageScreen

@Composable
fun SetupNavGraph(navController: NavHostController, startDestination: String) {

    NavHost(
        navController = navController,
        startDestination = startDestination // Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onBookSelect = {
                    navController.navigate(Screen.Details.passId(it))
                },
                onCreateClick = {
                    navController.navigate(Screen.Manage.passId())
                },
                bottomBar = {
                    BottomBar(navController)
                }
            )
        }
        composable(
            route = Screen.Manage.route,
            arguments = listOf(
                navArgument(
                    name = BOOK_ID_ARG
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val id = it.arguments?.getInt(BOOK_ID_ARG) ?: -1
            ManageScreen(
                id = id,
                onBackClick = { navController.navigateUp() },
                bottomBar = {
                    BottomBar(navController)
                }
            )
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument(
                    name = BOOK_ID_ARG
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            val id = it.arguments?.getInt(BOOK_ID_ARG) ?: 0
            DetailsScreen(
                onEditClick = {
                    navController.navigate(Screen.Manage.passId(id))
                },
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}