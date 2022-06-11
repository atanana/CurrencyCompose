package com.atanana.currencycompose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.currencycompose.data.CurrencyRepository
import com.atanana.currencycompose.domain.Currency
import com.atanana.currencycompose.preferences.CurrencyPreferences
import com.atanana.currencycompose.preferences.PreferencesProvider
import com.atanana.currencycompose.preferences.SelectedCurrencies
import com.atanana.currencycompose.ui.CurrencyAppActions
import com.atanana.currencycompose.ui.selector.CurrencySelectorState
import com.atanana.currencycompose.ui.table.CurrencyRow
import com.atanana.currencycompose.ui.table.CurrencySelectorItem
import com.atanana.currencycompose.ui.table.CurrencyTableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val preferencesProvider: PreferencesProvider
) : ViewModel(), CurrencyAppActions {

    var state by mutableStateOf<MainState>(MainState.Loading)
        private set

    private lateinit var conversions: Map<Currency, Double>

    init {
        preferencesProvider.preferences
            .runningFold(Pair<CurrencyPreferences?, CurrencyPreferences?>(null, null)) { (_, second), current -> Pair(second, current) }
            .onEach { (old, new) -> processPreferences(old, new) }
            .catch {
                Timber.e(it)
                state = MainState.Error("Something is wrong!")
            }
            .launchIn(viewModelScope)
    }

    private suspend fun processPreferences(old: CurrencyPreferences?, new: CurrencyPreferences?) {
        new ?: return
        if (old?.mainCurrency != new.mainCurrency) {
            state = MainState.Loading
            conversions = repository.getConversions(new.mainCurrency)
        }
        state = mapPreferencesToState(new)
    }

    private fun mapPreferencesToState(preferences: CurrencyPreferences): MainState {
        val allCurrencies = conversions.keys.toList()
        val currencySelectorState = CurrencySelectorState(preferences.amount, preferences.mainCurrency)

        val amount = preferences.amount.toDoubleOrNull() ?: 0.0
        val currencyRows = getCurrencyRows(amount, preferences.selectedCurrencies)
        val currencySelectorItems = getCurrencySelectorItems(preferences.selectedCurrencies)
        val currencyTableState = CurrencyTableState(currencyRows, currencySelectorItems)

        return MainState.Data(currencySelectorState, currencyTableState, allCurrencies)
    }

    private fun getCurrencyRows(amount: Double, selectedCurrencies: SelectedCurrencies): List<CurrencyRow> {
        val currencies = when (selectedCurrencies) {
            SelectedCurrencies.Default -> conversions
            is SelectedCurrencies.Selected -> conversions.filter { selectedCurrencies.currencies.contains(it.key) }
        }
        return currencies.map { (currency, value) -> CurrencyRow(currency, value * amount) }
    }

    private fun getCurrencySelectorItems(selectedCurrencies: SelectedCurrencies): List<CurrencySelectorItem> = when (selectedCurrencies) {
        SelectedCurrencies.Default -> conversions.keys.map { CurrencySelectorItem(it, true) }
        is SelectedCurrencies.Selected -> conversions.keys.map { CurrencySelectorItem(it, selectedCurrencies.currencies.contains(it)) }
    }

    override fun onAmountChanged(amount: String) {
        viewModelScope.launch {
            preferencesProvider.setAmount(amount)
        }
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModelScope.launch {
            preferencesProvider.setMainCurrency(currency)
        }
    }

    override fun onCurrenciesListChanged(currencies: List<Currency>) {
        viewModelScope.launch {
            preferencesProvider.setSelectedCurrencies(currencies)
        }
    }
}

sealed class MainState {

    object Loading : MainState()

    @Immutable
    data class Error(val message: String) : MainState()

    @Immutable
    data class Data(
        val currencySelectorState: CurrencySelectorState,
        val currenciesTableState: CurrencyTableState,
        val allCurrencies: List<Currency>
    ) : MainState()
}