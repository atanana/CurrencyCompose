package com.atanana.currencycompose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.currencycompose.data.CurrencyRepository
import com.atanana.currencycompose.domain.Currency
import com.atanana.currencycompose.ui.CurrencyAppActions
import com.atanana.currencycompose.ui.selector.CurrencySelectorState
import com.atanana.currencycompose.ui.table.CurrencyRow
import com.atanana.currencycompose.ui.table.CurrencySelectorItem
import com.atanana.currencycompose.ui.table.CurrencyTableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val DEFAULT_AMOUNT = "1"

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel(), CurrencyAppActions {

    var state by mutableStateOf<MainState>(MainState.Loading)
        private set

    private lateinit var conversions: Map<Currency, Double>
    private var selectedCurrencies = emptySet<Currency>()

    init {
        loadConversions(DEFAULT_AMOUNT, Currency("USD"))
    }

    private fun loadConversions(amount: String, currency: Currency) {
        loadConversions(currency) {
            state = createDefaultState(amount, currency)
        }
    }

    private inline fun loadConversions(currency: Currency, crossinline block: () -> Unit) {
        viewModelScope.launch {
            try {
                state = MainState.Loading
                conversions = repository.getConversions(currency)
                block()
                recalculateCurrencies()
            } catch (e: Exception) {
                Timber.e(e)
                state = MainState.Error("Cannot load data!")
            }
        }
    }

    private fun createDefaultState(amount: String, currency: Currency): MainState.Data {
        val allCurrencies = conversions.keys.toList()
        selectedCurrencies = allCurrencies.toSet()
        val currencySelectorState = CurrencySelectorState(amount, currency)
        val currencyTableState = CurrencyTableState(emptyList(), emptyList())
        return MainState.Data(currencySelectorState, currencyTableState, allCurrencies)
    }

    private fun updateConversions(currency: Currency) {
        val oldState = state as? MainState.Data ?: return
        loadConversions(currency) {
            val amount = oldState.currencySelectorState.amount
            state = oldState.copy(currencySelectorState = CurrencySelectorState(amount, currency))
        }
    }

    private fun recalculateCurrencies(block: ((MainState.Data) -> MainState.Data)? = null) = mapData { data ->
        val newState = if (block != null) block(data) else data
        val amount = newState.currencySelectorState.amount.toDoubleOrNull() ?: 0.0
        val currencyRows = conversions
            .filter { selectedCurrencies.contains(it.key) }
            .map { (currency, value) -> CurrencyRow(currency, value * amount) }
        val currencySelectorItems = newState.allCurrencies.map { CurrencySelectorItem(it, selectedCurrencies.contains(it)) }
        newState.copy(currenciesTableState = CurrencyTableState(currencyRows, currencySelectorItems))
    }

    private fun mapData(block: ((MainState.Data) -> MainState.Data)) {
        val state = state
        if (state is MainState.Data) {
            this.state = block(state)
        }
    }

    override fun onAmountChanged(amount: String) {
        recalculateCurrencies { currentState ->
            val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
            currentState.copy(currencySelectorState = newCurrencySelectorState)
        }
    }

    override fun onCurrencySelected(currency: Currency) {
        updateConversions(currency)
    }

    override fun onCurrenciesListChanged(currencies: List<Currency>) {
        selectedCurrencies = currencies.toSet()
        recalculateCurrencies()
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