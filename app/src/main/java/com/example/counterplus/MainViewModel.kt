package com.example.counterplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CounterState(
    val count: Int = 0,
    val isAutoMode: Boolean = false,
    val autoIncrementInterval: Long = 3000L
)

class MainViewModel : ViewModel() {

    // private counterstate
    private val _counterState = MutableStateFlow(CounterState())

    //public facing counterstate
    val counterState: StateFlow<CounterState> = _counterState.asStateFlow()

    private var autoIncrementJob: Job? = null

    // increment counter
    fun increment() {
        _counterState.value = _counterState.value.copy(
            count = _counterState.value.count + 1
        )
    }

    // decrement counter
    fun decrement() {
        _counterState.value = _counterState.value.copy(
            count = _counterState.value.count - 1
        )
    }

    // reset counter
    fun reset() {
        _counterState.value = _counterState.value.copy(count = 0)
    }

    // toggle auto mode
    fun toggleAutoMode() {
        val newAutoMode = !_counterState.value.isAutoMode
        _counterState.value = _counterState.value.copy(isAutoMode = newAutoMode)

        if (newAutoMode) {
            startAutoIncrement()
        } else {
            stopAutoIncrement()
        }
    }

    // update auto interval
    fun updateInterval(intervalSeconds: Int) {
        val intervalMillis = intervalSeconds * 1000L
        _counterState.value = _counterState.value.copy(
            autoIncrementInterval = intervalMillis
        )

        // restart if it's running
        if (_counterState.value.isAutoMode) {
            stopAutoIncrement()
            startAutoIncrement()
        }
    }

    // start auto coroutine
    private fun startAutoIncrement() {
        autoIncrementJob = viewModelScope.launch {
            while (true) {
                delay(_counterState.value.autoIncrementInterval)
                increment()
            }
        }
    }

    // stop auto coroutine
    private fun stopAutoIncrement() {
        autoIncrementJob?.cancel()
        autoIncrementJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoIncrement()
    }
}