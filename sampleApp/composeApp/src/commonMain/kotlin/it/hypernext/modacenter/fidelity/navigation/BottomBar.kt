package it.hypernext.modacenter.fidelity.navigation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.baseline_card_membership_24
import it.hypernext.modacenter.fidelity.baseline_card_membership_24_trasp
import org.jetbrains.compose.resources.painterResource

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
        val value by rememberInfiniteTransition().animateFloat(
            initialValue = 1.dp.value,
            targetValue = 30.dp.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        var route = Screen.Home.route
        var selected = currentDestination?.hierarchy?.any { it.route == route } == true
        NavigationBarItem(
            selected = selected,
            onClick = {
                navController.navigate(route)
            },
            icon = {
                if (selected) {
                    Image(

                        modifier = Modifier.size(value.dp),
                        painter = painterResource(Res.drawable.baseline_card_membership_24),
                        contentDescription = "appleIcon"
                    )
                } else {
                    Image(
                        modifier = Modifier.size(value.dp),
                        painter = painterResource(Res.drawable.baseline_card_membership_24_trasp),
                        contentDescription = "appleIcon"
                    )
                }
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