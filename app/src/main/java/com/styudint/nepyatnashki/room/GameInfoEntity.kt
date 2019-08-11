package com.styudint.nepyatnashki.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.styudint.nepyatnashki.data.GameInfo

@Entity
class GameInfoEntity {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    var timestamp: Long = 0
    var time: Long = 0
    var ended: Boolean = false
    var userUid: String? = null

    constructor()

    constructor(info: GameInfo) {
        timestamp = info.timestamp
        time = info.time
        ended = info.ended
        userUid = info.userUid
    }
}