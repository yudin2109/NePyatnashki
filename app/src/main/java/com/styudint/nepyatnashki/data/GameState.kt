package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface GameState {
    fun moveUp()
    fun moveDown()
    fun moveRight()
    fun moveLeft()

    fun handleTap(value: Int)

    // Permutation which represents game state, 15 corresponds to the empty block
    fun permutation(): List<Int>

    fun stopWatch(): LiveData<Long>
    fun moves(): LiveData<Int>

    fun start()

    fun subscribe(listener: GameStateListener)
    fun unsubscribe(listener: GameStateListener)
}