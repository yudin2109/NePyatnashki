package com.styudint.nepyatnashki.common

interface GameState {
    fun moveUp()
    fun moveDown()
    fun moveRight()
    fun moveLeft()

    // Permutation which represents game state, 15 corresponds to the empty block
    fun permutation(): List<Int>

    fun moves(): Int

    fun isSolved(): Boolean

    fun moveLog(): String
    fun amountOfMoves(): Int
}