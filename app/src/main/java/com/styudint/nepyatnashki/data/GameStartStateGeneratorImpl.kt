package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class GameStartStateGeneratorImpl @Inject constructor() : GameStartStateGenerator {
    var width = 4
    var height = 4
    private var permutationSize: Int = width * height

    override fun changeSizes(nextWidth: Int, nextHeight: Int) {
        width = nextWidth
        height = nextHeight
        permutationSize = width * height
    }

    override var gameState: AndroidGameState? = null

    override fun generate(): LiveData<AndroidGameState> {
        val liveData = MutableLiveData<AndroidGameState>()

        GlobalScope.launch {
            var permutation = generatePermutation(permutationSize)
            while (!isValidPermutation(permutation)) {
                permutation = generatePermutation(permutationSize)
            }
            gameState = AndroidGameStateImpl(permutation, width, height).also {
                liveData.postValue(it)
            }
        }

        return liveData
    }

    private fun emptyTileIndex() = permutationSize - 1

    private fun generatePermutation(size: Int): ArrayList<Int> {
        val result = ArrayList<Int>()

        for (i in 0 until(size)) {
            result.add(i)
        }

        result.shuffle()

        return result
    }

    private fun isValidPermutation(permutation: List<Int>): Boolean {
        return countInversions(permutation) % 2 == permutationSize % 2
    }

    private fun countInversions(list: List<Int>): Int {
        var result = 0
        for (i in list.indices) {
            if (list[i] == emptyTileIndex()) {
                result += i / width + 1
            } else {
                for (j in i + 1 until list.size) {
                    if (list[j] < list[i])
                        ++result
                }
            }
        }
        return result
    }
}