package com.styudint.nepyatnashki.settings

import com.styudint.nepyatnashki.R

enum class ControlMode {
    CLICKS, SWIPES
}

val ControlModeInfo: HashMap<ControlMode, Int> = hashMapOf(
    ControlMode.CLICKS to R.string.control_mode_clicks_string,
    ControlMode.SWIPES to R.string.control_mode_swipes_string
)

interface SettingsManagerListener {
    fun settingsChanged()
}

interface SettingsManager {
    fun controlMode(): ControlMode
    fun changeControlMode(mode: ControlMode)

    fun subscribe(listener: SettingsManagerListener)
    fun unsubscribe(listener: SettingsManagerListener)
}