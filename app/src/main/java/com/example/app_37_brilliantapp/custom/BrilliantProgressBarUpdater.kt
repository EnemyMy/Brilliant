package com.example.app_37_brilliantapp.custom

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.app_37_brilliantapp.custom.BrilliantProgressBar
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.util.toDateOrToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class BrilliantProgressBarUpdater(owner: LifecycleOwner): LifecycleEventObserver {

    private val lifecycle = owner.lifecycle
    private val lifecycleScope = owner.lifecycleScope
    private var progressUpdater = lifecycleScope.launch {}
    private var timeUpdater = lifecycleScope.launch {}
    private lateinit var progressCoroutine: suspend CoroutineScope.() -> Unit
    private lateinit var timeCoroutine: suspend CoroutineScope.() -> Unit
    @Volatile private var currentProgress = 0


    fun startUpdate(progressBar: BrilliantProgressBar, diamond: CurrentDiamond) {
        Log.e("ProgressBarUpdater:", "startUpdate")
        lifecycle.addObserver(this)
        val start = diamond.start.toDateOrToday().time
        val deadline = diamond.deadline.toDateOrToday().time
        val currentTime = Date().time
        val millisecondsInOnePercent = (deadline - start) / 100
        currentProgress =
                if ((deadline - start) <= 0 || currentTime > deadline) 100
                else (((currentTime - start).toFloat() / (deadline - start)) * 100).roundToInt()
        //no need to launch coroutines
        if (currentProgress == 100) {
            progressBar.setProgress(currentProgress)
            progressBar.setTimeLeft(deadline)
        }
        else {
            progressCoroutine = {
                Log.e("ProgressCoroutine:", "Launched!")
                Log.e("Progress:", "$currentProgress")
                while (currentProgress <= 100) {
                    progressBar.setProgress(currentProgress)
                    currentProgress++
                    Log.e("Progress:", "$currentProgress")
                    delay(millisecondsInOnePercent)
                }
            }
            timeCoroutine = {
                Log.e("TimeCoroutine:", "Launched!")
                while (currentProgress <= 100) {
                    progressBar.setTimeLeft(deadline)
                    delay(1000)
                }
            }
            launchCoroutines()
        }
    }

    fun stopUpdate() {
        progressUpdater.cancel()
        timeUpdater.cancel()
    }

    private fun launchCoroutines() {
        progressUpdater = lifecycleScope.launch(Dispatchers.Main, block = progressCoroutine)
        timeUpdater = lifecycleScope.launch(Dispatchers.Main, block = timeCoroutine)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.e("ProgressBarUpdater:", "onStateChanged: ${event.name}")
        if (event == Lifecycle.Event.ON_PAUSE) {
            stopUpdate()
        }
        else if (event == Lifecycle.Event.ON_RESUME) {
            if (progressUpdater.isCancelled) {
                launchCoroutines()
            }
        }
    }

}