package com.styudint.nepyatnashki.data

interface GameState {
    fun moveUp()
    fun moveDown()
    fun moveRight()
    fun moveLeft()

    fun handleTap(value: Int)

    // Permutation which represents game state, 15 corresponds to the empty block
    fun permutation(): List<Int>

    fun moves(): Int

    fun isSolved(): Boolean

    fun startTime(): Long
    fun gameTime(): Long

    fun moveLog(): String
    fun amountOfMoves(): Int

    fun start()
    fun stop()

    fun subscribe(listener: GameStateListener)
    fun unsubscribe(listener: GameStateListener)
}