package com.next.goldentimewearable.receiver

import android.content.Intent
import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType

object HeartRate {
    private var bpm = 70.0;

    fun setBpm(newBpm: Double) {
        bpm = newBpm
    }
    fun getBpm(): Double {
        return bpm
    }
}

class HealthWatchService : PassiveListenerService() {
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
            HeartRate.setBpm(it.value)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}