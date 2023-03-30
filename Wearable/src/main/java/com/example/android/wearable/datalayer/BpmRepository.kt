package com.example.android.wearable.datalayer

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BpmRepository {
    fun getBPM():Flow<Double>  = flow {
        // emit(MobileNodesObject.getBPM())

         while(true){
                    emit(MobileNodesObject.getBPM())
                    delay(1000)
                    Log.d("HEALTH WATCH", "emit"+ MobileNodesObject.getBPM()+"~")

                }


    }
}
