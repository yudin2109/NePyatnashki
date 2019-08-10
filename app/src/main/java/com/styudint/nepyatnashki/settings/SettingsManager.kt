package com.styudint.nepyatnashki.settings

import androidx.room.TypeConverter
import com.styudint.nepyatnashki.R

enum class ControlMode {
    CLICKS, SWIPES
}

val ControlModeInfo: HashMap<ControlMode, Int> = hashMapOf(
    ControlMode.CLICKS to R.string.control_mode_clicks_string,
    ControlMode.SWIPES to R.string.control_mode_swipes_string
)

class ControlModeConverter {
    @TypeConverter
    fun controlModeToInt(mode: ControlMode): Int = ControlModeInfo[mode]!!

    @TypeConverter
    fun intToControlMode(value: Int): ControlMode {
        ControlModeInfo.forEach {
            if (it.value == value) {
                return it.key
            }
        }
        throw IllegalStateException("Something went wrong, not found ControlMode for int value: $value")
    }
}

interface SettingsManagerListener {
    fun settingsChanged()
}

interface SettingsManager {
    fun controlMode(): ControlMode
    fun changeControlMode(mode: ControlMode)

    fun subscribe(listener: SettingsManagerListener)
    fun unsubscribe(listener: SettingsManagerListener)
}