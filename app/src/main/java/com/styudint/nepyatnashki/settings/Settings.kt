package com.styudint.nepyatnashki.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
class Settings {
    @PrimaryKey(autoGenerate = false) // We assume that we have only one instance of settings in database
    var uid: Int = 0

    @TypeConverters(ControlModeConverter::class)
    var controlMode: ControlMode = ControlMode.CLICKS // Default control mode is CLICKS
}