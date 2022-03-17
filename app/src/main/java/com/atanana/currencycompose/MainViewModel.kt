package com.atanana.currencycompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.currencycompose.data.Currency
import com.atanana.currencycompose.data.CurrencyRepository
import com.atanana.currencycompose.ui.CurrencyAppActions
import com.atanana.currencycompose.ui.CurrencyItem
import com.atanana.currencycompose.ui.CurrencySelectorState
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

    init {
        loadConversions(Currency("USD"))
    }

    private fun loadConversions(currency: Currency) {
        viewModelScope.launch {
            try {
                state = MainState.Loading
                conversions = repository.getConversions(currency)
                val amount = (state as? MainState.Data)?.currencySelectorState?.amount ?: DEFAULT_AMOUNT
                val allCurrencies = conversions.keys.toList()
                state = MainState.Data(CurrencySelectorState(amount, currency), emptyList(), allCurrencies)
                recalculateCurrencies()
            } catch (e: Exception) {
                Timber.e(e)
                state = MainState.Error("Cannot load data!")
            }
        }
    }

    private fun recalculateCurrencies(block: ((MainState.Data) -> MainState.Data)? = null) = mapData { data ->
        val newState = if (block != null) block(data) else data
        val amount = newState.currencySelectorState.amount.toDoubleOrNull() ?: 0.0
        val items = conversions.map { (currency, value) -> CurrencyItem(currency, value * amount) }
        newState.copy(currencies = items)
    }

    private fun mapData(block: ((MainState.Data) -> MainState.Data)) {
        val state = state
        if (state is MainState.Data) {
            this.state = block(state)
        }
    }

    override fun onAmountChanged(amount: String) {
        recalculateCurrencies() { currentState ->
            val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
            currentState.copy(currencySelectorState = newCurrencySelectorState)
        }
    }

    override fun onCurrencySelected(currency: Currency) {
        loadConversions(currency)
    }
}

sealed class MainState {

    object Loading : MainState()

    data class Error(val message: String) : MainState()

    data class Data(
        val currencySelectorState: CurrencySelectorState,
        val currencies: List<CurrencyItem>,
        val allCurrencies: List<Currency>
    ) : MainState()
}