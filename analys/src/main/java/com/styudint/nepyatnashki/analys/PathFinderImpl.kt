package com.styudint.nepyatnashki.analys

import com.styudint.nepyatnashki.common.*
import java.lang.IllegalStateException

class PathFinderImpl : PathFinder {
    override fun findAnyPath(gameState: GameState): List<Move> {
        val endCache = LocalCacheImpl()
        endCache.build(GameStateImpl(
            GameStateUtils.buildWinPermutation(
                gameState.width(),
                gameState.height()),
            gameState.width(),
            gameState.height()),
            17)

        val moves = ArrayList<Move>()

        val currentGameState = gameState.clone()

        var count = 0
        while (endCache.cacheDistance(currentGameState) == null) {
            count += 1
            if (count == 1000)
                throw IllegalStateException("Cannot find path")

            val useManhattanPotential = PermutationUtils.countRightPositions(gameState) > gameState.permutation().size * 3 / 4

            val potentialFunction: (GameState) -> Int = {
                if (useManhattanPotential)
                    GameStateUtils.calculatePositionPredictedPower(it)
                gameState.permutation().size - PermutationUtils.countRightPositions(it)
            }

            val randomMoves = randomWalk(currentGameState, 100, 32, potentialFunction)
            GameStateUtils.applyMoves(currentGameState, randomMoves)

            moves.addAll(randomMoves)
        }

        endCache.cacheMoves(currentGameState)?.forEach {
            moves.add(it)
        }

        return moves
    }

    private fun randomWalk(
        gameState: GameState,
        amountOfTries: Int,
        maxDepth: Int,
        potentialFunc: (GameState) -> Int)
    : ArrayList<Move> {
        var minPotential = potentialFunc(gameState)
        var optimalMoves = ArrayList<Move>()

        for (i in 0 until amountOfTries) {
            val currentGameState = gameState.clone()
            val currentMoves = ArrayList<Move>()

            for (j in 0 until maxDepth) {
                val move = GameStateUtils.findAvailableMoves(currentGameState).random()
                GameStateUtils.applyMove(currentGameState, move)
                currentMoves.add(move)

                val currentPositionPower = potentialFunc(currentGameState)
                if (currentPositionPower < minPotential) {
                    optimalMoves = currentMoves.clone() as ArrayList<Move>
                    minPotential = currentPositionPower
                }
            }
        }

        return optimalMoves
    }
}