package com.styudint.nepyatnashki.data.repositories

import androidx.lifecycle.LiveData
import com.styudint.nepyatnashki.data.GameInfo

interface StatisticsRepository {
    fun statistics(): LiveData<List<GameInfo>>
    fun saveGame(info: GameInfo)
}