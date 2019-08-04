package com.styudint.nepyatnashki.dagger

import android.content.Context
import androidx.room.Room
import com.styudint.nepyatnashki.data.StatisticsRepository
import com.styudint.nepyatnashki.data.StatisticsRepositoryImpl
import com.styudint.nepyatnashki.room.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(private var context: Context) {
    @Provides
    @Singleton
    fun statisticsRepository(impl: StatisticsRepositoryImpl): StatisticsRepository {
        return impl
    }

    @Provides
    @Singleton
    fun appDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "ne_pyatnashki")
            .fallbackToDestructiveMigration()
            .build()
    }
}