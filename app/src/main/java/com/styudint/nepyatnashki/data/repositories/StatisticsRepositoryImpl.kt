package com.styudint.nepyatnashki.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.styudint.nepyatnashki.data.GameInfo
import com.styudint.nepyatnashki.room.AppDatabase
import com.styudint.nepyatnashki.room.GameInfoEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private var appDatabase: AppDatabase
) : StatisticsRepository {
    private val statistics = ArrayList<GameInfoEntity>()
    private val initialization = GlobalScope.launch {
        loadData()
    }
    private val statsLiveData = MutableLiveData<List<GameInfoEntity>>()

    override fun statistics(): LiveData<List<GameInfo>> = Transformations.map(statsLiveData) { it -> run {
        val result = ArrayList<GameInfo>()
        it.forEach {
            result.add(GameInfo(it.timestamp, it.time, it.nMoves,it.ended, it.userUid))
        }
        result
    }}

    private fun loadData() {
        statistics.clear()
        appDatabase.gameInfoDao().getAll().forEach {
            statistics.add(it)
        }
        statsLiveData.postValue(statistics)
    }

    override fun saveGame(info: GameInfo) {
        val infoToInsert = GameInfoEntity(info)
        GlobalScope.launch {
            appDatabase.gameInfoDao().insert(infoToInsert)
            loadData()
        }
    }
}