package com.atanana.currencycompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.currencycompose.data.network.Api
import com.atanana.currencycompose.data.network.ConversionsResult
import com.atanana.currencycompose.ui.CurrencyAppActions
import com.atanana.currencycompose.ui.CurrencyItem
import com.atanana.currencycompose.ui.CurrencySelectorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel(), CurrencyAppActions {

    var state by mutableStateOf<MainState>(MainState.Loading)
        private set

    private lateinit var conversions: ConversionsResult

    init {
        viewModelScope.launch {
            try {
                conversions = api.getConversions("USD")
                val allCurrencies = conversions.conversionRates.keys.toList()
                state = MainState.Data(CurrencySelectorState("1", "USD"), emptyList(), allCurrencies)
                recalculateCurrencies()
            } catch (e: Exception) {
                Timber.e(e)
                state = MainState.Error("Cannot load data!")
            }
        }
    }

    private fun recalculateCurrencies(block: ((MainState.Data) -> MainState.Data)? = null) {
        val state = state
        if (state is MainState.Data) {
            val newState = if (block != null) block(state) else state
            val amount = newState.currencySelectorState.amount.toDoubleOrNull() ?: 0.0
            val items = conversions.conversionRates.map { (currency, value) -> CurrencyItem(currency, value * amount) }
            this.state = newState.copy(currencies = items)
        }
    }

    override fun onAmountChanged(amount: String) {
        recalculateCurrencies() { currentState ->
            val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
            currentState.copy(currencySelectorState = newCurrencySelectorState)
        }
    }

    override fun onCurrencySelected(currency: String) {

    }
}

sealed class MainState {

    object Loading : MainState()

    data class Error(val message: String) : MainState()

    data class Data(
        val currencySelectorState: CurrencySelectorState,
        val currencies: List<CurrencyItem>,
        val allCurrencies: List<String>
    ) : MainState()
}