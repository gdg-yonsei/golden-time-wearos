package com.next.goldentimewearable.model

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartRateRepository(context: Context) {
    private val passiveMonitoringClient: PassiveMonitoringClient

    init {
        val healthClient = HealthServices.getClient(context)
        passiveMonitoringClient = healthClient.passiveMonitoringClient

        checkCapability()
        startWatching()
    }

    private fun checkCapability() {
        CoroutineScope(Dispatchers.Default).launch {
            val capabilities = passiveMonitoringClient.getCapabilities()
            val heartRateWatchable = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring

            Log.d("HEALTH WATCH", heartRateWatchable.toString())
        }
    }

    fun startWatching() {
        val config = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .build()

        passiveMonitoringClient.setPassiveListenerServiceAsync(HealthWatchService::class.java, config)
    }

    fun stopWatching() {
        passiveMonitoringClient.clearPassiveListenerServiceAsync()
    }
}

class HealthWatchService : PassiveListenerService() {
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
            Log.d("HEALTH WATCH", "type : ${it.dataType.name} / value : ${it.value}")
        }
    }
}