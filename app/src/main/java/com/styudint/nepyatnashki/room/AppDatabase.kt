package com.styudint.nepyatnashki.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameInfoEntity::class],
    exportSchema = false,
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameInfoDao(): GameInfoDao
}