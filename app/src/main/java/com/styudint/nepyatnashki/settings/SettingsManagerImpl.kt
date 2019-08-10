package com.styudint.nepyatnashki.settings

import com.styudint.nepyatnashki.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsManagerImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : SettingsManager {
    private val listeners = ArrayList<SettingsManagerListener>()

    private var settings = Settings()

    init {
        GlobalScope.launch {
            with (appDatabase.settingsDao()) {
                val fromDatabase = get()
                if (fromDatabase == null) {
                    insert(settings)
                } else {
                    settings = fromDatabase
                    notifySettingsChanged()
                }
            }
        }
    }

    override fun changeControlMode(mode: ControlMode) {
        settings.controlMode = mode
        notifySettingsChanged()
    }

    override fun controlMode(): ControlMode {
        return settings.controlMode
    }

    private fun saveToDatabase() {
        GlobalScope.launch {
            appDatabase.settingsDao().update(settings)
        }
    }

    override fun subscribe(listener: SettingsManagerListener) {
        listeners.add(listener)
        listener.settingsChanged()
    }

    override fun unsubscribe(listener: SettingsManagerListener) {
        listeners.remove(listener)
    }

    private fun notifySettingsChanged() {
        saveToDatabase()
        listeners.forEach {
            it.settingsChanged()
        }
    }
}