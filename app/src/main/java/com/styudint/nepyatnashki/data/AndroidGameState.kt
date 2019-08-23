package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface AndroidGameState : GameState {
    fun stopWatch(): LiveData<Long>
}