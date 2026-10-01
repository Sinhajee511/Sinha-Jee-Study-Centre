package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun WatermarkOverlay(
    modifier: Modifier = Modifier,
    text: String = "SINHA JEE STUDY CENTRE"
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Gray.copy(alpha = 0.08f),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.rotate(-30f)
        )
    }
}
