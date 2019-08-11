package com.styudint.nepyatnashki.dagger

import com.styudint.nepyatnashki.GameActivity
import com.styudint.nepyatnashki.SettingsPage
import com.styudint.nepyatnashki.SignInPage
import com.styudint.nepyatnashki.StatisticsPageViewModel
import com.styudint.nepyatnashki.fragments.AccountInfoFragment
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
}