package com.styudint.nepyatnashki.analys

import com.styudint.nepyatnashki.common.GameStateImpl
import com.styudint.nepyatnashki.common.PermutationUtils

fun main() {
    val permutation = ArrayList<Int>()
    for (i in 0 until 16) {
        permutation.add(i)
    }
    permutation.shuffle()

    while (PermutationUtils.countInvertions(permutation) % 2 == 1) {
        permutation.shuffle()
    }

    val gameState = GameStateImpl(permutation, 4, 4)

    val pathFinder = PathFinderImpl()
    val moves = pathFinder.findAnyPath(gameState)
    println(moves.size)
}