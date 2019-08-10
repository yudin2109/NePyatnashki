package com.styudint.nepyatnashki.dagger

import android.content.Context
import androidx.room.Room
import com.styudint.nepyatnashki.data.GameStartStateGenerator
import com.styudint.nepyatnashki.data.GameStartStateGeneratorImpl
import com.styudint.nepyatnashki.data.repositories.StatisticsRepository
import com.styudint.nepyatnashki.data.repositories.StatisticsRepositoryImpl
import com.styudint.nepyatnashki.room.AppDatabase
import com.styudint.nepyatnashki.settings.SettingsManager
import com.styudint.nepyatnashki.settings.SettingsManagerImpl
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

    @Provides
    @Singleton
    fun providesGameStartStateGenerator(impl: GameStartStateGeneratorImpl): GameStartStateGenerator = impl

    @Provides
    @Singleton
    fun providesSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl
}