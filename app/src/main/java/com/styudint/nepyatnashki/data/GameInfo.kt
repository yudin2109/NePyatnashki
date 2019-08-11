package com.styudint.nepyatnashki.data

data class GameInfo(
    var timestamp: Long,    // Moment of game begin
    var time: Long,       // Time of a game in milliseconds
    var ended: Boolean,
    var userUid: String?
)