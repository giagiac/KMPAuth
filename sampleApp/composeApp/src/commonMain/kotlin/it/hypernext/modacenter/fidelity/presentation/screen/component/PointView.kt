package it.hypernext.modacenter.fidelity.presentation.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.hypernext.modacenter.fidelity.Res
import it.hypernext.modacenter.fidelity.api.datamodel.Score
import it.hypernext.modacenter.fidelity.sharp_money_bag_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun PointView(
    score: Score,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(size = 12.dp))
        .clickable { onClick() }
    ) {
        Box(modifier = Modifier.size(60.dp)) {
//            CoilImage(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(size = 12.dp))
//                    .padding(6.dp)
//                    .size(40.dp),
//                imageModel = { "https://cdn.pixabay.com/photo/2023/03/13/20/13/button-7850704_640.png" },
//                imageOptions = ImageOptions(
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.Center
//                )
//            )
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(Res.drawable.sharp_money_bag_24),
                contentDescription = "saved money"
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(3f).padding(vertical = 6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = score.points,
                maxLines = 1,
                //overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.displayMedium.fontSize
            )
            Text(
                text = score.dataScan,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontWeight = FontWeight.Medium
            )
        }
    }
}