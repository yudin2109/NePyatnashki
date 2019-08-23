package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class AndroidGameStateImpl(
    private var permutation: ArrayList<Int>,
    private val width: Int,
    private val height: Int
) : AndroidGameState {
    companion object {
        const val DEFAULT_DELAY_TIME = 16L
    }

    private val listeners = ArrayList<GameStateListener>()
    private val moves = MutableLiveData<Int>()
    private val stopWatch = MutableLiveData<Long>()

    private var amountOfMoves = 0
    private var moveLog = ""
    private var isGameOver = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var gameTime: Long = 0

    override fun moveUp() {
        move(-1, 0)
    }

    override fun moveRight() {
        move(0, 1)
    }

    override fun moveLeft() {
        move(0, -1)
    }

    override fun moveDown() {
        move(1, 0)
    }

    override fun handleTap(value: Int) {
        val position = Pair(value % 4, value / 4)
        val emptyPosition = findEmpty()
        val diff = Math.abs(position.first - emptyPosition.first) + Math.abs(position.second - emptyPosition.second)
        if (diff == 1) {
            move(emptyPosition.first - position.first, emptyPosition.second - position.second)
        }
    }

    override fun subscribe(listener: GameStateListener) {
        listeners.add(listener)
    }

    override fun unsubscribe(listener: GameStateListener) {
        listeners.remove(listener)
    }

    override fun permutation(): List<Int> = permutation

    override fun start() {
        moves.postValue(amountOfMoves)
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

    override fun moves(): Int = amountOfMoves

    override fun amountOfMoves(): Int = amountOfMoves

    override fun stopWatch(): LiveData<Long> = stopWatch

    private fun move(dX: Int, dY: Int) {
        val position = findEmpty()
        val swpPosition = Pair(position.first - dX, position.second - dY)
        if (!isValidPosition(swpPosition)) {
            return
        }

        val from = realValue(position)
        val to = realValue(swpPosition)
        val tmp = permutation[from]
        permutation[from] = permutation[to]
        permutation[to] = tmp

        when (dX) {
            -1 -> moveLog += "L"
            1 -> moveLog += "R"
            else -> {
                when (dY) {
                    -1 -> moveLog += "U"
                    1 -> moveLog += "D"
                }
            }
        }

        incrementMoves()
        notifyChanges()
    }

    private fun incrementMoves() {
        amountOfMoves += 1
        moves.postValue(amountOfMoves)
    }

    override fun moveLog(): String {
        return moveLog
    }

    override fun isSolved(): Boolean {
        for (i in 0 until GameStartStateGeneratorImpl.getSize()) {
            if (i != permutation[i])
                return false
        }
        return true
    }

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

    private fun realValue(position: Pair<Int, Int>): Int = position.first + position.second * 4

    private fun isValidPosition(position: Pair<Int, Int>): Boolean {
        if (position.first !in 0 until width) return false
        if (position.second !in 0 until height) return false
        return true
    }

    private fun findEmpty(): Pair<Int, Int> = positionByValue(GameStartStateGeneratorImpl.getSize() - 1)

    private fun positionByValue(value: Int): Pair<Int, Int> {
        for (i in 0 ..GameStartStateGeneratorImpl.getSize()) {
            if (permutation[i] == value) {
                return Pair(i % 4, i / 4)
            }
        }
        throw IllegalStateException("Cannot find empty block")
    }
}