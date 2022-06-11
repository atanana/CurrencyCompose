package com.atanana.currencycompose.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES_CURRENCY = "currency"

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    private val Context.currencyDataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_CURRENCY)

    @Singleton
    @Provides
    fun provideDataStore(context: Context): DataStore<Preferences> = context.currencyDataStore
}