package it.hypernext.modacenter.fidelity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.hypernext.modacenter.fidelity.presentation.screen.account.AccountScreen
import it.hypernext.modacenter.fidelity.presentation.screen.card.CardScreen
import it.hypernext.modacenter.fidelity.presentation.screen.details.DetailsScreen
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginScreen
import it.hypernext.modacenter.fidelity.presentation.screen.manage.ManageScreen
import it.hypernext.modacenter.fidelity.presentation.screen.offer.OfferScreen

@Composable
fun SetupNavGraph(navController: NavHostController, startDestination: String) {

    NavHost(
        navController = navController,
        startDestination = startDestination // Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Card.route) {
                        popUpTo(Screen.Login.route) { // toglie dalla storia il LOGIN
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.Card.route) {
            CardScreen(
                bottomBar = {
                    BottomBar(navController)
                }
            )
        }
        composable(route = Screen.Account.route) {
            AccountScreen(
                bottomBar = {
                    BottomBar(navController)
                },
                onLogout = {
                    navController.popBackStack(Screen.Login.route, true)
                }
            )
        }
        composable(route = Screen.Offer.route) {
            OfferScreen(
                bottomBar = {
                    BottomBar(navController)
                },
                onOfferSelect = {
                    navController.navigate(Screen.Details.passUrl(it))
                },
            )
        }
//        composable(route = Screen.Home.route) {
//            CardScreen(
//                onBookSelect = {
//                    navController.navigate(Screen.Details.passId(it))
//                },
//                onCreateClick = {
//                    navController.navigate(Screen.Manage.passId())
//                },
//                bottomBar = {
//                    BottomBar(navController)
//                }
//            )
//        }
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
                    name = URL_ARG
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val id = it.arguments?.getString(URL_ARG) ?: ""
            DetailsScreen(
                id = id,
                onEditClick = {
                    navController.navigate(Screen.Details.passUrl(id))
                },
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}