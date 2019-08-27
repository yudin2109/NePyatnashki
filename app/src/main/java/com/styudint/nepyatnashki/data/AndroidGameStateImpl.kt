package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.styudint.nepyatnashki.common.GameStateImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidGameStateImpl(
    permutation: ArrayList<Int>,
    width: Int,
    height: Int
) : GameStateImpl(permutation, width, height), AndroidGameState {
    companion object {
        const val DEFAULT_DELAY_TIME = 16L
    }

    private val listeners = ArrayList<AndroidGameStateListener>()
    private val stopWatch = MutableLiveData<Long>()

    private var isGameOver = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var gameTime: Long = 0

    override fun handleTap(value: Int) {
        val position = Pair(value / 4, value % 4)
        val emptyPosition = findEmpty()
        val diff = Math.abs(position.first - emptyPosition.first) + Math.abs(position.second - emptyPosition.second)
        if (diff == 1) {
            move(emptyPosition.first - position.first, emptyPosition.second - position.second)
            notifyChanges()
        }
    }

    override fun moveDown(): AndroidGameState {
        super.moveDown()
        notifyChanges()
        return this
    }

    override fun moveLeft(): AndroidGameState {
        super.moveLeft()
        notifyChanges()
        return this
    }

    override fun moveRight(): AndroidGameState {
        super.moveRight()
        notifyChanges()
        return this
    }

    override fun moveUp(): AndroidGameState {
        super.moveUp()
        notifyChanges()
        return this
    }

    override fun subscribe(listener: AndroidGameStateListener) {
        listeners.add(listener)
    }

    override fun unsubscribe(listener: AndroidGameStateListener) {
        listeners.remove(listener)
    }

    override fun start() {
        stopWatch.postValue(0)
        startTime = System.currentTimeMillis()
        GlobalScope.launch {
            while (!isGameOver) {
                val time = System.currentTimeMillis() - startTime
                stopWatch.postValue(time)
                delay(DEFAULT_DELAY_TIME)
            }
        }
    }

    override fun stop() {
        if (!isGameOver) {
            isGameOver = true
            endTime = System.currentTimeMillis()
            gameTime = endTime - startTime
            stopWatch.postValue(gameTime)
        }
    }

    override fun startTime(): Long = startTime

    override fun gameTime(): Long = gameTime

    override fun stopWatch(): LiveData<Long> = stopWatch

    private fun notifyChanges() {
        if (isSolved()) {
            notifyGameStateChanged()
            stop()
            notifySolved()
        } else {
            notifyGameStateChanged()
        }
    }

    private fun notifyGameStateChanged() {
        listeners.forEach {
            it.gameStateChanged()
        }
    }

    private fun notifySolved() {
        listeners.forEach {
            it.solved()
        }
    }
}