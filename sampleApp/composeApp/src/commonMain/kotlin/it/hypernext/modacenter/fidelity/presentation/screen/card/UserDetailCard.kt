package it.hypernext.modacenter.fidelity.presentation.screen.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.api.datamodel.UserDetail
import it.hypernext.modacenter.fidelity.date_of_birth
import it.hypernext.modacenter.fidelity.name
import it.hypernext.modacenter.fidelity.surname
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserDetailCard(userDetail: UserDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            userDetail.name.let {
                Text(
                    text = "${stringResource(Res.string.name)}: ",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            userDetail.surname.let {
                Text(
                    text = "${stringResource(Res.string.surname)}: ",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            userDetail.dateOfBirth.let {
                Text(
                    text = "${stringResource(Res.string.date_of_birth)}: ",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}