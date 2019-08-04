package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface StatisticsRepository {
    fun statistics(): LiveData<List<GameInfo>>
    fun saveGame(info: GameInfo)
}