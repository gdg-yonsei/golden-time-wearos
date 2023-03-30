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

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.health.services.client.data.DataType
import androidx.lifecycle.*
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
class MainActivity : ComponentActivity() {
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }
    private val clientDataViewModel by viewModels<ClientDataViewModel>()
    private val bpmRepository : BpmRepository = BpmRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var doubleBpm : Double? = 10.0

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                bpmRepository.getBPM().collect{value ->
                    doubleBpm = value
                    Log.d("HEALTH WATCH", "HH BPM: $value")

                    setContent {
                        MainApp(
                            events = clientDataViewModel.events,
                            //image = clientDataViewModel.image,
                            onQueryDevicesThenSendMessage = ::onQueryDevicesThenSendMessage,
                            doubleBPM = doubleBpm
                            // onQueryMobileCameraClicked = ::onQueryMobileCameraClicked
                        )
                    }
                }
            }
        }


        fun mman() = runBlocking<Unit> {bpmRepository.getBPM().collect{
                value ->
                doubleBpm = value
                delay(5000)
            }
        }

        // mman()
        bpmRepository.getBPM().asLiveData().map { value -> doubleBpm = value }

        // var doubleBpm : Double? = getBpm().value
        if(doubleBpm==null){doubleBpm = 58.0}
        initialize() // start watching BPM
    }
    private fun initialize() {
        val repository = HeartRateRepository(this)
        repository.startWatching()

    }

    private fun onQueryDevicesThenSendMessage() {
        lifecycleScope.launch {
            try {
                val nodes = getCapabilitiesForReachableNodes()
                    .filterValues { MOBILE_CAPABILITY in it || WEAR_CAPABILITY in it }.keys
                // displayNodes(nodes)
                sendBpmMessageToMobile(nodes) // !! send message to mobile
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Querying nodes failed: $exception")
            }
        }
    }


    /**
     * Collects the capabilities for all nodes that are reachable using the [CapabilityClient].
     *
     * [CapabilityClient.getAllCapabilities] returns this information as a [Map] from capabilities
     * to nodes, while this function inverts the map so we have a map of [Node]s to capabilities.
     *
     * This form is easier to work with when trying to operate upon all [Node]s.
     */
    private suspend fun getCapabilitiesForReachableNodes(): Map<Node, Set<String>> =
        capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE)
            .await()
            // Pair the list of all reachable nodes with their capabilities
            .flatMap { (capability, capabilityInfo) ->
                capabilityInfo.nodes.map { it to capability }
            }
            // Group the pairs by the nodes
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            // Transform the capability list for each node into a set
            .mapValues { it.value.toSet() }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun sendBpmMessageToMobile(nodes: Set<Node>) {
        val stringMessage = "86 bpm";
        lifecycleScope.launch {
            try {
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, "", stringMessage.toByteArray(Charsets.UTF_8)) // !!!!!!!!!!!!!!!!!!!!!!!!!!
                            .await()
                    }
                }.awaitAll()

                Log.d(TAG, "Sent BPM successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Sending failed: $exception")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val WEAR_CAPABILITY = "wear"
        private const val MOBILE_CAPABILITY = "mobile"
    }
}
