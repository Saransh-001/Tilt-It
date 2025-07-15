package com.example.tiltit

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreManager {

    private val Context.dataStore by preferencesDataStore("game_prefs")

    private val HIGH_SCORE_KEY = intPreferencesKey("high_score")

    suspend fun saveHighScore(context: Context, score: Int) {
        context.dataStore.edit { prefs ->
            prefs[HIGH_SCORE_KEY] = score
        }
    }

    suspend fun getHighScore(context: Context): Int {
        val prefs = context.dataStore.data.first()
        return prefs[HIGH_SCORE_KEY] ?: 0
    }

}