package com.atanana.currencycompose.data

import com.atanana.currencycompose.data.network.Api
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val api: Api) {

    suspend fun getConversions(currency: Currency): Map<Currency, Double> =
        api.getConversions(currency.value).conversionRates.mapKeys { Currency(it.key) }
}

@JvmInline
value class Currency(val value: String)
