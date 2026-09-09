package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SubirBrandGradient
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta

@Composable
fun SubirLogoImage(
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    showGlowBorder: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (showGlowBorder) 6.dp else 0.dp,
                shape = RoundedCornerShape(size * 0.28f),
                ambientColor = SubirCyan,
                spotColor = SubirMagenta
            )
            .clip(RoundedCornerShape(size * 0.28f))
            .background(Color.Black)
            .then(
                if (showGlowBorder) {
                    Modifier.border(
                        width = 1.5.dp,
                        brush = SubirBrandGradient,
                        shape = RoundedCornerShape(size * 0.28f)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.subir_logo),
            contentDescription = "Subir Official Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun SubirBrandHeaderTitle(
    modifier: Modifier = Modifier,
    logoSize: Dp = 34.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SubirLogoImage(size = logoSize)
        Text(
            text = "SUBIR",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
