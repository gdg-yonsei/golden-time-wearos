package com.next.goldentimewearable.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.next.goldentimewearable.App
import com.next.goldentimewearable.repository.HeartRateRepository

class MainViewModel(
    private val heartRateRepository: HeartRateRepository = HeartRateRepository(App.context)
) : ViewModel() {
    private val _bpm = heartRateRepository.watchBpm()

    val bpm = _bpm.asLiveData()

    fun stopWatchingBpm() {
        heartRateRepository.stopWatching()
    }
}