package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface AndroidGameState : com.styudint.nepyatnashki.common.GameState {
    fun handleTap(value: Int)
    fun stopWatch(): LiveData<Long>

    fun startTime(): Long
    fun gameTime(): Long

    fun subscribe(listener: AndroidGameStateListener)
    fun unsubscribe(listener: AndroidGameStateListener)

    fun start()
    fun stop()
}