package com.styudint.nepyatnashki.analys

import com.styudint.nepyatnashki.common.GameState
import com.styudint.nepyatnashki.common.GameStateUtils
import com.styudint.nepyatnashki.common.Move
import java.lang.IllegalStateException
import java.util.concurrent.LinkedBlockingQueue

class LocalCacheImpl : LocalCache {
    private val distance = HashMap<String, Int>()

    override fun build(initialGameState: GameState, depth: Int) {
        distance[gameStateRepr(initialGameState)] = 0

        val queue = LinkedBlockingQueue<GameState>()
        queue.add(initialGameState.clone())

        while (queue.isNotEmpty()) {
            val top = queue.remove()
            val currentDistance = distance[gameStateRepr(top)] ?: continue

            if (currentDistance == depth)
                continue

            if (top.canMoveLeft()) {
                val copy = top.clone().moveLeft()
                val repr = gameStateRepr(copy)
                if (!distance.containsKey(repr)) {
                    distance[repr] = currentDistance + 1
                    queue.add(copy)
                }
            }

            if (top.canMoveRight()) {
                val copy = top.clone().moveRight()
                val repr = gameStateRepr(copy)
                if (!distance.containsKey(repr)) {
                    distance[repr] = currentDistance + 1
                    queue.add(copy)
                }
            }

            if (top.canMoveDown()) {
                val copy = top.clone().moveDown()
                val repr = gameStateRepr(copy)
                if (!distance.containsKey(repr)) {
                    distance[repr] = currentDistance + 1
                    queue.add(copy)
                }
            }

            if (top.canMoveUp()) {
                val copy = top.clone().moveUp()
                val repr = gameStateRepr(copy)
                if (!distance.containsKey(repr)) {
                    distance[repr] = currentDistance + 1
                    queue.add(copy)
                }
            }
        }
    }

    override fun cacheMoves(gameState: GameState): ArrayList<Move>? {
        val currentGameState = gameState.clone()
        var currentDistance = distance[gameStateRepr(currentGameState)] ?: throw IllegalStateException("Game state is not in local cache")
        val optimalMoves = ArrayList<Move>()

        while (currentDistance != 0) {
            for (move in GameStateUtils.findAvailableMoves(currentGameState)) {
                val checkState = currentGameState.clone()
                GameStateUtils.applyMove(checkState, move)
                val checkDistance = distance[gameStateRepr(checkState)] ?: continue
                if (checkDistance < currentDistance) {
                    currentDistance = checkDistance
                    GameStateUtils.applyMove(currentGameState, move)
                    optimalMoves.add(move)
                    break
                }
            }
        }
        return optimalMoves
    }

    override fun cacheDistance(gameState: GameState): Int? = distance[gameStateRepr(gameState)]

    private fun gameStateRepr(gameState: GameState) = gameState.permutation().joinToString("|")
}