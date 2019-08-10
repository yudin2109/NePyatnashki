package com.styudint.nepyatnashki.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.styudint.nepyatnashki.settings.Settings

@Database(
    entities = [GameInfoEntity::class, Settings::class],
    exportSchema = false,
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameInfoDao(): GameInfoDao
    abstract fun settingsDao(): SettingsDao
}