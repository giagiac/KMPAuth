package it.hypernext.modacenter.fidelity.presentation.screen.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.apple_logo
import it.hypernext.modacenter.fidelity.domain.User
import it.hypernext.modacenter.fidelity.email
import it.hypernext.modacenter.fidelity.ic_google
import it.hypernext.modacenter.fidelity.name
import it.hypernext.modacenter.fidelity.phone_number
import it.hypernext.modacenter.fidelity.sms
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            user.displayName?.let {
                Text(
                    text = "${stringResource(Res.string.name)}: ${user.displayName}",
                    style = MaterialTheme.typography.headlineLarge,

                    )
            }
            user.phoneNumber?.let {
                Text(
                    text = "${stringResource(Res.string.phone_number)}: ${user.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            user.email?.let {
                Text(
                    text = "${stringResource(Res.string.email)}: ${user.email}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (!user.photoURL.isNullOrEmpty()) {
                CoilImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp))
                        .size(120.dp),
                    imageModel = { user.photoURL },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                )
            } else if (user.providerId.lowercase() == "apple.com") {
                Card(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.apple_logo),
                        contentDescription = "apple.com",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else if (user.providerId.lowercase() == "google.com") {
                Card(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.ic_google),
                        contentDescription = "google",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else if (user.providerId.lowercase() == "firebase") {
                Card(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.sms),
                        contentDescription = "google",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}