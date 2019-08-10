package com.styudint.nepyatnashki.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.styudint.nepyatnashki.settings.Settings

@Dao
interface SettingsDao {
    @Insert
    fun insert(settings: Settings)

    @Update
    fun update(settings: Settings)

    @Query("select * from settings limit 1")
    fun get(): Settings?
}