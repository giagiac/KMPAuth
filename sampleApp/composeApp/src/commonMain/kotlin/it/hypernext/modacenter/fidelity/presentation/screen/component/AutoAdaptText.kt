package it.hypernext.modacenter.fidelity.presentation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

var shrunkFontSize = 72.sp
val calculateIntrinsics = @Composable {
    ParagraphIntrinsics(
        text = "inputValue",
        style = TextStyle(
            fontSize = shrunkFontSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 80.sp,
            textAlign = TextAlign.Center
        ),
        density = LocalDensity.current,
        fontFamilyResolver = createFontFamilyResolver()
    )
}

var intrinsics = calculateIntrinsics()