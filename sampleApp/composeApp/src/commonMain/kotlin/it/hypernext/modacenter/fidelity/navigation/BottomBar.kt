package it.hypernext.modacenter.fidelity.navigation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.account
import it.hypernext.modacenter.fidelity.baseline_account_box_24
import it.hypernext.modacenter.fidelity.baseline_card_membership_24
import it.hypernext.modacenter.fidelity.baseline_favorite_24
import it.hypernext.modacenter.fidelity.card
import it.hypernext.modacenter.fidelity.offers
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
            initialValue = 25.dp.value,
            targetValue = 30.dp.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route ==  Screen.Card.route } == true,
            onClick = {
                navController.navigate( Screen.Card.route)
            },
            icon = {
                val selected = currentDestination?.hierarchy?.any { it.route ==  Screen.Card.route } == true
                Icon(
                    modifier = Modifier.size(value.dp).alpha(if(selected) 1f else 0.38f),
                    painter = painterResource(Res.drawable.baseline_card_membership_24),
                    contentDescription = "card"
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.card) // destination.iconText
                )
            })
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Details.route } == true,
            onClick = { navController.navigate(Screen.Details.route) },
            icon = {
                val selected = currentDestination?.hierarchy?.any { it.route ==  Screen.Details.route } == true
                Icon(
                    modifier = Modifier.size(25.dp).alpha(if(selected) 1f else 0.38f),
                    painter = painterResource(Res.drawable.baseline_account_box_24),
                    contentDescription = "account"
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.account)
                )
            })
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Details.route } == true,
            onClick = { navController.navigate(Screen.Details.route) },
            icon = {
                val selected = currentDestination?.hierarchy?.any { it.route ==  Screen.Details.route } == true
                Icon(
                    modifier = Modifier.size(25.dp).alpha(if(selected) 1f else 0.38f),
                    painter = painterResource(Res.drawable.baseline_favorite_24),
                    contentDescription = "offer"
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.offers)
                )
            })
    }
}