package com.styudint.nepyatnashki.analys

import com.styudint.nepyatnashki.common.GameState
import com.styudint.nepyatnashki.common.Move

interface LocalCache {
    fun build(initialGameState: GameState, depth: Int)
    fun cacheDistance(gameState: GameState): Int?
    fun cacheMoves(gameState: GameState): ArrayList<Move>?
}