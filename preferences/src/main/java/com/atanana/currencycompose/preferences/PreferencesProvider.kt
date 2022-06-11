package com.atanana.currencycompose.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesProvider @Inject constructor(private val preferences: DataStore<Preferences>) {
}