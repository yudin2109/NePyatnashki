package com.styudint.nepyatnashki.common

abstract class PermutationUtils {
    companion object {
        fun countInvertions(list: List<Int>): Int {
            var count = 0
            for (i in 0 until list.size) {
                for (j in i + 1 until list.size) {
                    if (list[i] > list[j])
                        count += 1
                }
            }
            return count
        }

        fun countRightPositions(gameState: GameState)
            = gameState.permutation().foldIndexed(0) { index, acc, i -> if (index == i) acc + 1 else acc }
    }
}