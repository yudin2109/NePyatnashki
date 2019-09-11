package com.styudint.nepyatnashki.common

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

open class GameStateImpl(
    private var permutation: ArrayList<Int>,
    private val width: Int,
    private val height: Int
) : GameState {
    init {
        if (permutation.size != width * height)
            throw IllegalArgumentException("size should be equal to multiplication width on height")
    }

    protected var isGameOver = false
    private var moveLog = ""
    private var amountOfMoves = 0

    override fun moveUp(): GameState {
        move(-1, 0)
        return this
    }

    override fun moveRight(): GameState {
        move(0, 1)
        return this
    }

    override fun moveLeft(): GameState {
        move(0, -1)
        return this
    }

    override fun moveDown(): GameState {
        move(1, 0)
        return this
    }

    override fun height(): Int = height
    override fun width(): Int = width

    override fun canMoveUp() = canMove(-1, 0)
    override fun canMoveRight() = canMove(0, 1)
    override fun canMoveLeft() = canMove(0, -1)
    override fun canMoveDown() = canMove(1, 0)

    override fun permutation(): List<Int> = permutation

    override fun clone(): GameState = GameStateImpl(ArrayList(permutation), width, height)

    protected open fun canMove(dX: Int, dY: Int): Boolean {
        val position = findEmpty()
        return isValidPosition(Pair(position.first - dX, position.second - dY))
    }

    protected fun move(dX: Int, dY: Int) {
        if (isGameOver)
            return

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

        amountOfMoves += 1

        when (dY) {
            -1 -> moveLog += "L"
            1 -> moveLog += "R"
            else -> {
                when (dX) {
                    -1 -> moveLog += "U"
                    1 -> moveLog += "D"
                }
            }
        }
    }

    override fun moves(): Int = amountOfMoves

    override fun amountOfMoves(): Int = amountOfMoves

    override fun moveLog(): String {
        return moveLog
    }

    override fun isSolved(): Boolean {
        for (i in 0 until permutation.size) {
            if (i != permutation[i])
                return false
        }
        return true
    }

    protected fun findEmpty(): Pair<Int, Int> = positionByValue(permutation.size - 1)

    private fun positionByValue(value: Int): Pair<Int, Int> {
        for (i in 0 until width * height) {
            if (permutation[i] == value) {
                return Pair(i / 4, i % 4)
            }
        }
        throw IllegalStateException("Cannot find empty block")
    }

    private fun realValue(position: Pair<Int, Int>): Int = position.first * 4 + position.second

    private fun isValidPosition(position: Pair<Int, Int>): Boolean {
        if (position.first !in 0 until width) return false
        if (position.second !in 0 until height) return false
        return true
    }
}