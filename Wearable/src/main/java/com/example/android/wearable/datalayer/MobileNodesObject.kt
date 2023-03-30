package com.example.android.wearable.datalayer

object MobileNodesObject {
    var doubleBPM = 62.0
    fun setBPM(newBPM: Double){
        doubleBPM = newBPM
    }
    fun getBPM() : Double{
        return doubleBPM
    }
}


