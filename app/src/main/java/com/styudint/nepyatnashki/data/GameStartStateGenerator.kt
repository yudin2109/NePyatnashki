package com.styudint.nepyatnashki.data

import androidx.lifecycle.LiveData

interface GameStartStateGenerator {
    fun generate(): LiveData<AndroidGameState>
}