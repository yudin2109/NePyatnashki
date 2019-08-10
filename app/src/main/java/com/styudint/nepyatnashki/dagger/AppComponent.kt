package com.styudint.nepyatnashki.dagger

import com.styudint.nepyatnashki.GameActivity
import com.styudint.nepyatnashki.SettingsPage
import com.styudint.nepyatnashki.StatisticsPageViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface AppComponent {
    fun inject(statisticsPage: StatisticsPageViewModel)
    fun inject(gameActivity: GameActivity)
    fun inject(settingsPage: SettingsPage)
}