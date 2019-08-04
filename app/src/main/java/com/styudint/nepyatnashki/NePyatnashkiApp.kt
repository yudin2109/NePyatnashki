package com.styudint.nepyatnashki

import android.app.Application
import com.styudint.nepyatnashki.dagger.AppComponent
import com.styudint.nepyatnashki.dagger.DaggerAppComponent
import com.styudint.nepyatnashki.dagger.MainModule

class NePyatnashkiApp : Application() {
    var appComponent: AppComponent = DaggerAppComponent.builder()
        .mainModule(MainModule(this))
        .build()
}