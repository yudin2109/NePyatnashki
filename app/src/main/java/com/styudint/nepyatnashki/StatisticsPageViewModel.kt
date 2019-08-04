package com.styudint.nepyatnashki

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.styudint.nepyatnashki.data.StatisticsRepository
import javax.inject.Inject

class StatisticsPageViewModel(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var statisticsRepository: StatisticsRepository

    init {
        app as NePyatnashkiApp
        app.appComponent.inject(this)
    }

    var statistics = statisticsRepository.statistics()
}