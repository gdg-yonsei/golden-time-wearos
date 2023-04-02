package com.next.goldentimewearable.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await

private const val MOBILE_CAPABILITY = "mobile"
private const val WEAR_CAPABILITY = "wear"

class TransactionRepository(context: Context) {
    private val messageClient by lazy { Wearable.getMessageClient(context) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(context) }

    private suspend fun getCapabilitiesForReachableNodes(): Map<Node, Set<String>> =
        capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE).await()
            .flatMap { (capability, capabilityInfo) ->
                capabilityInfo.nodes.map { it to capability }
            }
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            .mapValues { it.value.toSet() }

    suspend fun sendMessage(message: String) {
        try {
            val nodes = getCapabilitiesForReachableNodes()
                .filterValues { MOBILE_CAPABILITY in it || WEAR_CAPABILITY in it }.keys

            nodes.forEach { node ->
                messageClient.sendMessage(node.id, "", message.toByteArray(Charsets.UTF_8))
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.e("Golden Time", "Sending failed: $exception")
        }
    }
}