package com.styudint.nepyatnashki.analys

import com.styudint.nepyatnashki.common.GameState
import com.styudint.nepyatnashki.common.Move

interface PathFinder {
    fun findAnyPath(gameState: GameState): List<Move>
}