package com.atanana.currencycompose.data.network

interface Api {

    suspend fun getConversions(): ConversionsResult
}