package com.atanana.currencycompose.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES_CURRENCY = "currency"

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun provideDataStore(context: Context) = context.preferencesDataStoreFile(PREFERENCES_CURRENCY)
}