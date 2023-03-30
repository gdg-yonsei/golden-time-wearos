package com.example.android.wearable.datalayer

import android.content.Context
import android.util.Log
import androidx.health.services.client.*
import androidx.health.services.client.data.*
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.HealthEvent
import androidx.health.services.client.data.PassiveListenerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.runBlocking

class HeartRateRepository(context: Context) {
    private val passiveMonitoringClient: PassiveMonitoringClient
    val measureClient : MeasureClient
    init {
        val healthClient = HealthServices.getClient(context)
        measureClient = healthClient.measureClient
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
        Log.d("HEALTH WATCH", "Start watching a BPM")

        passiveMonitoringClient.setPassiveListenerServiceAsync(HealthWatchService::class.java, config)

    }

    fun stopWatching() {
        passiveMonitoringClient.clearPassiveListenerServiceAsync()
    }
}

class HealthWatchService : PassiveListenerService() {

    init{
        Log.d("HEALTH WATCH", "Service init")

    }
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        Log.d("HEALTH WATCH", "New Data received!")

        dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
            Log.d("HEALTH WATCH", "type : ${it.dataType.name} / value : ${it.value}")
            MobileNodesObject.setBPM(it.value)
        }
      }
    override fun onHealthEventReceived(event: HealthEvent) {
        runBlocking {
            Log.d("HEALTH WATCH", "onHealthEventReceived received with type: ${event.type}")
            // HealthServicesManager.getInstance(applicationContext).recordHealthEvent(event)
            super.onHealthEventReceived(event)
        }
    }
    override fun onUserActivityInfoReceived(info: UserActivityInfo) {
        Log.d("HEALTH WATCH", "User info received?")

    }
}
