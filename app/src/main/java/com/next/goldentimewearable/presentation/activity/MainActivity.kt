package com.next.goldentimewearable.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.next.goldentimewearable.model.HeartRateRepository
import com.next.goldentimewearable.presentation.theme.GoldenTimeWearableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }

        initialize()
    }

    private fun initialize() {
        val repository = HeartRateRepository(this)
        repository.startWatching()
    }
}

@Composable
fun WearApp() {
    GoldenTimeWearableTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Greeting()
            Spacer(modifier = Modifier.height(20.dp))
            Activator()
        }
    }
}

@Composable
fun Greeting() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = "Golden Time"
    )
}

@Composable
fun Activator() {
    Button(
        onClick = { Log.d("HEALTH WATCH", "CLICKED!") }
    ) {
        Text(text = "Watch")
    }
}
