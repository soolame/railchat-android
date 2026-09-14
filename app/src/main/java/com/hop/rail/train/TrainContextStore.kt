package com.hop.rail.train

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.trainContextDataStore by preferencesDataStore(name = "train_context")

/**
 * Local-only persistence for [TrainContext]. Both fields are nullable and
 * skippable — a passenger boarding in a hurry will not fill in a form.
 */
class TrainContextStore(private val context: Context) {

    private object Keys {
        val TRAIN_NUMBER = stringPreferencesKey("train_number")
        val COACH_ID = stringPreferencesKey("coach_id")
        val BOARDING_TIMESTAMP = longPreferencesKey("boarding_timestamp")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val trainContext: Flow<TrainContext> = context.trainContextDataStore.data.map { prefs ->
        TrainContext(
            trainNumber = prefs[Keys.TRAIN_NUMBER],
            coachId = prefs[Keys.COACH_ID],
            boardingTimestamp = prefs[Keys.BOARDING_TIMESTAMP] ?: 0L
        )
    }

    /** Whether the onboarding train/coach entry screen has already been shown once. */
    suspend fun hasCompletedOnboarding(): Boolean =
        context.trainContextDataStore.data.first()[Keys.ONBOARDING_COMPLETE] ?: false

    /** Marks the onboarding step seen without recording a train/coach (user tapped Skip). */
    suspend fun skipOnboarding() {
        context.trainContextDataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETE] = true
        }
    }

    suspend fun update(trainNumber: String?, coachId: String?, boardingTimestamp: Long) {
        context.trainContextDataStore.edit { prefs ->
            if (trainNumber != null) prefs[Keys.TRAIN_NUMBER] = trainNumber else prefs.remove(Keys.TRAIN_NUMBER)
            if (coachId != null) prefs[Keys.COACH_ID] = coachId else prefs.remove(Keys.COACH_ID)
            prefs[Keys.BOARDING_TIMESTAMP] = boardingTimestamp
            prefs[Keys.ONBOARDING_COMPLETE] = true
        }
    }
}
