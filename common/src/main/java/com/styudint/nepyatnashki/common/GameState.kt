package com.styudint.nepyatnashki.common

interface GameState {
    fun moveUp(): GameState
    fun moveDown(): GameState
    fun moveRight(): GameState
    fun moveLeft(): GameState

    fun canMoveUp(): Boolean
    fun canMoveLeft(): Boolean
    fun canMoveDown(): Boolean
    fun canMoveRight(): Boolean

    fun height(): Int
    fun width(): Int

    // Permutation which represents game state, 15 corresponds to the empty block
    fun permutation(): List<Int>

    fun moves(): Int

    fun isSolved(): Boolean

    fun moveLog(): String
    fun amountOfMoves(): Int

    // Creates a copy of game state
    fun clone(): GameState
}