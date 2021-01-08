package com.example.wast.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Coroutines {
    fun debounce(
        scope: CoroutineScope,
        timeMs: Long = 300L,
        function: () -> Unit,
    ): () -> Unit {
        var job: Job? = null
        return {
            job?.cancel()
            job = scope.launch {
                delay(timeMs)
                function()
            }
        }
    }
}