package com.styudint.nepyatnashki.dagger

import com.styudint.nepyatnashki.*
import com.styudint.nepyatnashki.adapters.GalleryAdapter
import com.styudint.nepyatnashki.fragments.AccountInfoFragment
import com.styudint.nepyatnashki.gameviews.GameDrawingThread
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface AppComponent {
    fun inject(statisticsPage: StatisticsPageViewModel)
    fun inject(gameActivity: GameActivity)
    fun inject(settingsPage: SettingsPage)
    fun inject(accountInfoFragment: AccountInfoFragment)
    fun inject(signInPage: SignInPage)
    fun inject(menuActivity: MenuActivity)
    fun inject(adapter: GalleryAdapter)
    fun inject(gameThread: GameDrawingThread)
}