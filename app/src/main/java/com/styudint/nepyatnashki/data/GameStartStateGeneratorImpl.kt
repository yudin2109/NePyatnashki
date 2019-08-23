package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

class GameStartStateGeneratorImpl @Inject constructor() : GameStartStateGenerator {
    companion object {
        const val HEIGHT = 4
        const val WIDTH = 4

        fun getSize(): Int = HEIGHT * WIDTH
    }

    override fun generate(): LiveData<AndroidGameState> {
        val liveData = MutableLiveData<AndroidGameState>()

        GlobalScope.launch {
            var permutation = generatePermutation(getSize())
            while (!isValidPermutation(permutation)) {
                permutation = generatePermutation(getSize())
            }
            liveData.postValue(AndroidGameStateImpl(permutation, WIDTH, HEIGHT))
        }

        return liveData
    }

    private fun generatePermutation(size: Int): ArrayList<Int> {
        val result = ArrayList<Int>()

        for (i in 0 until(size)) {
            result.add(i)
        }

        result.shuffle()

        return result
    }

    private fun isValidPermutation(permutation: List<Int>): Boolean {
        return countInversions(permutation) % 2 == 0
    }

    private fun countInversions(list: List<Int>): Int {
        var result = 0
        for (i in list.indices) {
            if (list[i] == 15) {
                result += i / 4 + 1
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