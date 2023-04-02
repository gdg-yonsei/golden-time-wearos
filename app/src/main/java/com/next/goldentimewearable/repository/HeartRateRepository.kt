package com.next.goldentimewearable.repository

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import com.next.goldentimewearable.receiver.HealthWatchService
import com.next.goldentimewearable.receiver.HeartRate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

class HeartRateRepository(context: Context) {
    private val passiveMonitoringClient: PassiveMonitoringClient by lazy {
        HealthServices.getClient(context).passiveMonitoringClient
    }

    init {
        startWatching()
    }


    private suspend fun isHeartRateWatchable(): Boolean {
        val capabilities = passiveMonitoringClient.getCapabilities()

        return DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
    }

    private fun startWatching() {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isHeartRateWatchable()) return@launch

            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .build()

            passiveMonitoringClient.setPassiveListenerServiceAsync(
                HealthWatchService::class.java,
                config
            )
        }
    }

    fun stopWatching() {
        passiveMonitoringClient.clearPassiveListenerServiceAsync()
    }

    fun watchBpm() = flow {
        while (true) {
            val bpm = HeartRate.getBpm()
            emit(bpm)

            delay(3000)
        }
    }
}
