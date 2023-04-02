package com.next.goldentimewearable.ui.components.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.next.goldentimewearable.R

@Composable
fun Heartbeat() {
    val infiniteTransition = rememberInfiniteTransition()

    val heartbeatAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        modifier = Modifier
            .scale(heartbeatAnimation)
            .size(60.dp),
        painter = painterResource(id = R.drawable.image_heart),
        contentDescription = null
    )
}