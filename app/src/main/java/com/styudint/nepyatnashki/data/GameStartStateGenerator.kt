package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface GameStartStateGenerator {
    fun generate(): LiveData<AndroidGameState>
    fun changeSizes(nextWidth: Int, nextHeight: Int)

    var gameState: AndroidGameState?
}