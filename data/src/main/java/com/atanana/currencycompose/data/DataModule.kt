package com.atanana.currencycompose.data

import com.atanana.currencycompose.data.network.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val API_KEY = "995f51168b2950a6b4f31365"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideApi(): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/v6/$API_KEY/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }
}