package com.next.goldentimewearable.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.next.goldentimewearable.ui.components.main.Heartbeat

@Composable
fun MainScreen(model: MainViewModel = viewModel()) {
    val bpm by model.bpm.observeAsState()

    DisposableEffect(Unit) {
        onDispose {
            model.stopWatchingBpm()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4DED5))
            .padding(30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Golden Time",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF984f4f)
        )
        Heartbeat()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Current BPM : $bpm", fontSize = 12.sp, color = Color(0xFF984f4f))
            Spacer(Modifier.height(3.dp))
            Text(text = "You are stable", fontSize = 12.sp, color = Color(0xFF984f4f))
        }
    }
}