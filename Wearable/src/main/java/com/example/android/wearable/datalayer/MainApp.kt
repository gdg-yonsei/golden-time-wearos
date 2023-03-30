/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.wearable.datalayer

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.android.wearable.datalayer.R
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun MainApp(
    events: List<Event>,
    // image: Bitmap?,
    onQueryDevicesThenSendMessage: () -> Unit,
    doubleBPM : Double?
    // onQueryMobileCameraClicked: () -> Unit
) {
    val scalingLazyListState = rememberScalingLazyListState()
    val infiniteTransition = rememberInfiniteTransition()

    // val doubleBPM = MobileNodesObject.getBPM()
    Scaffold(
        modifier = Modifier.background(Color(0xFFF4DED5)),
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) },
        timeText = { TimeText() }
    ) {

        ScalingLazyColumn(
            state = scalingLazyListState,
            contentPadding = PaddingValues(
                horizontal = 18.dp,
                vertical = 10.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

               item{
                   HeartbeatAnimation(infiniteTransition)

               }

                item{
                    Text("Golden Time",
                        modifier = Modifier.fillMaxWidth().padding(top=20.dp),
                        textAlign = TextAlign.Center,
                        //fontWeight = 3
                        fontWeight = FontWeight.Bold
                        )
                }
                item{
                    Text("Current BPM: $doubleBPM",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }
               }

            /*
             item {
                Button(
                    onClick = onQueryDevicesThenSendMessage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.query_other_devices))
                }
            }

            * */

        }
}


@Composable
fun HeartbeatAnimation(infiniteTransition: InfiniteTransition) {
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
            .size(90.dp)
            .padding(top =30.dp)
            ,
        painter = painterResource(id = R.drawable.img_heart),
        contentDescription = "",
        // colorFilter = ColorFilter.tint(Color.White)
    )
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun MainAppPreviewEvents() {
    MainApp(
        events = listOf(
            Event(
                title = R.string.data_item_changed,
                text = "Event 1"
            ),
            Event(
                title = R.string.data_item_deleted,
                text = "Event 2"
            ),
            Event(
                title = R.string.data_item_unknown,
                text = "Event 3"
            ),
            Event(
                title = R.string.message,
                text = "Event 4"
            ),
            Event(
                title = R.string.data_item_changed,
                text = "Event 5"
            ),
            Event(
                title = R.string.data_item_deleted,
                text = "Event 6"
            )
        ),

        onQueryDevicesThenSendMessage = {},
        doubleBPM = 61.0

        // onQueryMobileCameraClicked = {}
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun MainAppPreviewEmpty() {
    MainApp(
        events = emptyList(),
        //image = null,
        onQueryDevicesThenSendMessage = {},
        doubleBPM = 61.0
        // onQueryMobileCameraClicked = {}
    )
}
