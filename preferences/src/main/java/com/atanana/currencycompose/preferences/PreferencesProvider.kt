package com.atanana.currencycompose.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.atanana.currencycompose.domain.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFERENCES_CURRENCY = "currency"

private val KEY_AMOUNT = floatPreferencesKey("amount")
private val KEY_MAIN_CURRENCY = stringPreferencesKey("main_currency")
private val KEY_SELECTED_CURRENCIES = stringPreferencesKey("selected_currencies")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_CURRENCY)

@Singleton
class PreferencesProvider @Inject constructor(private val context: Context) {

    suspend fun setAmount(amount: Float) {
        savePreference { preferences -> preferences[KEY_AMOUNT] = amount }
    }

    suspend fun setMainCurrency(currency: Currency) {
        savePreference { preferences -> preferences[KEY_MAIN_CURRENCY] = currency.value }
    }

    suspend fun setSelectedCurrencies(currencies: Set<Currency>) {
        savePreference { preferences -> preferences[KEY_SELECTED_CURRENCIES] = currencies.joinToString(separator = ",") }
    }

    private suspend inline fun savePreference(noinline block: (MutablePreferences) -> Unit) {
        context.dataStore.edit(block)
    }

    val preferences: Flow<CurrencyPreferences> = context.dataStore.data.map { preferences ->
        val amount = preferences[KEY_AMOUNT] ?: 1f
        val mainCurrency = Currency(preferences[KEY_MAIN_CURRENCY] ?: "USD")
        val selectedCurrencies = extractSelectedCurrencies(preferences)
        CurrencyPreferences(amount, mainCurrency, selectedCurrencies)
    }

    private fun extractSelectedCurrencies(preferences: Preferences): SelectedCurrencies {
        val currenciesString = preferences[KEY_SELECTED_CURRENCIES] ?: return SelectedCurrencies.Default
        val currencies = currenciesString.split(',').map { Currency(it) }.toSet()
        return SelectedCurrencies.Selected(currencies)
    }
}

data class CurrencyPreferences(val amount: Float, val mainCurrency: Currency, val selectedCurrencies: SelectedCurrencies)

sealed class SelectedCurrencies {

    object Default : SelectedCurrencies()

    data class Selected(val currencies: Set<Currency>) : SelectedCurrencies()
}