package com.atanana.currencycompose.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.atanana.currencycompose.domain.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFERENCES_CURRENCY = "currency"

private val KEY_AMOUNT = stringPreferencesKey("amount")
private val KEY_MAIN_CURRENCY = stringPreferencesKey("main_currency")
private val KEY_SELECTED_CURRENCIES = stringPreferencesKey("selected_currencies")

private const val DEFAULT_AMOUNT = "1"
private const val DEFAULT_CURRENCY = "USD"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_CURRENCY)

@Singleton
class PreferencesProvider @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun setAmount(amount: String) {
        savePreference { preferences -> preferences[KEY_AMOUNT] = amount }
    }

    suspend fun setMainCurrency(currency: Currency) {
        savePreference { preferences -> preferences[KEY_MAIN_CURRENCY] = currency.value }
    }

    suspend fun setSelectedCurrencies(currencies: List<Currency>) {
        val currenciesString = currencies.joinToString(separator = ",") { it.value }
        savePreference { preferences -> preferences[KEY_SELECTED_CURRENCIES] = currenciesString }
    }

    private suspend inline fun savePreference(noinline block: (MutablePreferences) -> Unit) {
        context.dataStore.edit(block)
    }

    val preferences: Flow<CurrencyPreferences> = context.dataStore.data.map { preferences ->
        val amount = preferences[KEY_AMOUNT] ?: DEFAULT_AMOUNT
        val mainCurrency = Currency(preferences[KEY_MAIN_CURRENCY] ?: DEFAULT_CURRENCY)
        val selectedCurrencies = extractSelectedCurrencies(preferences)
        CurrencyPreferences(amount, mainCurrency, selectedCurrencies)
    }

    private fun extractSelectedCurrencies(preferences: Preferences): SelectedCurrencies {
        val currenciesString = preferences[KEY_SELECTED_CURRENCIES] ?: return SelectedCurrencies.Default
        val currencies = currenciesString.split(',').map { Currency(it) }.toSet()
        return SelectedCurrencies.Selected(currencies)
    }
}

data class CurrencyPreferences(val amount: String, val mainCurrency: Currency, val selectedCurrencies: SelectedCurrencies)

sealed class SelectedCurrencies {

    object Default : SelectedCurrencies()

    data class Selected(val currencies: Set<Currency>) : SelectedCurrencies()
}