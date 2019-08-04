package com.styudint.nepyatnashki.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameInfoDao {
    @Insert
    fun insert(info: GameInfoEntity)

    @Delete
    fun delete(info: GameInfoEntity)

    @Query("SELECT * FROM gameinfoentity ORDER BY timestamp DESC")
    fun getAll(): List<GameInfoEntity>
}