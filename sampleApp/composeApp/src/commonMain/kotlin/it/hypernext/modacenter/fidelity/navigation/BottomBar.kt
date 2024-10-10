package it.hypernext.modacenter.fidelity.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController) {

    val currentDestination = navController.currentBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
            )
            .height(70.dp),
    ) {
        var route = Screen.Home.route
        var selected = currentDestination?.hierarchy?.any { it.route == route } == true
        NavigationBarItem(
            selected = selected,
            onClick = { navController.navigate(route) },
            icon = {
//                val icon = if (selected) {
//                    Image(
//                        modifier = Modifier.size(20.dp),
//                        painter = painterResource(it.hypernext.modacenter.fidelity.Res.drawable.ic_google),
//                        contentDescription = "appleIcon"
//                    )
//                } else {
//                    Icons.Outlined.Home
//                }
//                Icon(
//                    imageVector = Res.drawable.ic_google,
//                    modifier = Modifier.size(16.dp),
//                    contentDescription = null
//                )
            },
            label = {
                Text(
                    text = "Home"//destination.iconText
                )
            })
        route = Screen.Details.route
        selected = currentDestination?.hierarchy?.any { it.route == route } == true
        NavigationBarItem(
            selected = selected,
            onClick = { navController.navigate(route) },
            icon = {
                val icon = if (selected) {
                    Icons.Default.AccountBox
                } else {
                    Icons.Outlined.AccountBox
                }
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Home"//destination.iconText
                )
            })
    }
}