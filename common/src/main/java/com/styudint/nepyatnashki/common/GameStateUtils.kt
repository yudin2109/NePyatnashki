package com.styudint.nepyatnashki.common

abstract class GameStateUtils {
    companion object {
        fun buildWinPermutation(width: Int, height: Int): ArrayList<Int> {
            val permutation = ArrayList<Int>()
            for (i in 0 until width * height)
                permutation.add(i)
            return permutation
        }

        fun applyMove(gameState: GameState, move: Move) {
            when (move) {
                Move.UP -> gameState.moveUp()
                Move.RIGHT -> gameState.moveRight()
                Move.DOWN -> gameState.moveDown()
                Move.LEFT -> gameState.moveLeft()
            }
        }

        fun findAvailableMoves(gameState: GameState): ArrayList<Move> {
            val availableMoves = ArrayList<Move>()
            if (gameState.canMoveUp())
                availableMoves.add(Move.UP)
            if (gameState.canMoveRight())
                availableMoves.add(Move.RIGHT)
            if (gameState.canMoveDown())
                availableMoves.add(Move.DOWN)
            if (gameState.canMoveLeft())
                availableMoves.add(Move.LEFT)
            return availableMoves
        }

        fun applyMoves(gameState: GameState, moves: List<Move>) {
            moves.forEach {
                applyMove(gameState, it)
            }
        }

        fun positionForIndex(index: Int, width: Int): Pair<Int, Int> {
            return Pair(index / width, index % width)
        }

        fun calculatePositionPredictedPower(gameState: GameState): Int {
            var result = 0
            gameState.permutation().forEachIndexed { index, value ->
                val position = positionForIndex(value, gameState.width())
                val currentPosition = positionForIndex(index, gameState.width())
                val dX = position.first - currentPosition.first
                val dY = position.second - currentPosition.second
                result += Math.abs(dX) + Math.abs(dY)
            }
            return result
        }
    }
}