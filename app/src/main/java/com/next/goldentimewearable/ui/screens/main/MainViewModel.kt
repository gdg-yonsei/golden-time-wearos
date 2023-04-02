package com.next.goldentimewearable.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.next.goldentimewearable.App
import com.next.goldentimewearable.repository.HeartRateRepository
import com.next.goldentimewearable.repository.TransactionRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val heartRateRepository: HeartRateRepository = HeartRateRepository(App.context),
    private val transactionRepository: TransactionRepository = TransactionRepository(App.context),
) : ViewModel() {
    private val _bpm = heartRateRepository.watchBpm()

    val bpm = _bpm.asLiveData()

    fun stopWatchingBpm() {
        heartRateRepository.stopWatching()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            transactionRepository.sendMessage(message)
        }
    }
}