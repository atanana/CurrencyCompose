package com.atanana.currencycompose.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("latest/{currency}")
    suspend fun getConversions(@Path("currency") currency: String): ConversionsResult
}