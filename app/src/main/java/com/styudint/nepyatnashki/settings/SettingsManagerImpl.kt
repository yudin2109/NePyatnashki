package com.styudint.nepyatnashki.settings

import javax.inject.Inject

/**
 * Currently just for testing ;)
 */

class SettingsManagerImpl @Inject constructor() : SettingsManager {
    private val listeners = ArrayList<SettingsManagerListener>()

    private var controlMode = ControlMode.SWIPES

    override fun changeControlMode(mode: ControlMode) {
        controlMode = mode
        notifySettingsChanged()
    }

    override fun controlMode(): ControlMode {
        return controlMode
    }

    override fun subscribe(listener: SettingsManagerListener) {
        listeners.add(listener)
        listener.settingsChanged()
    }

    override fun unsubscribe(listener: SettingsManagerListener) {
        listeners.remove(listener)
    }

    private fun notifySettingsChanged() {
        listeners.forEach {
            it.settingsChanged()
        }
    }
}