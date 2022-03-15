package com.atanana.currencycompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.currencycompose.data.network.Api
import com.atanana.currencycompose.data.network.ConversionsResult
import com.atanana.currencycompose.ui.CurrencyAppActions
import com.atanana.currencycompose.ui.CurrencyItem
import com.atanana.currencycompose.ui.CurrencySelectorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel(), CurrencyAppActions {

    private val state = MutableStateFlow(MainState(CurrencySelectorState("1", "USD"), emptyList()))
    val stateFlow: StateFlow<MainState> = state

    private lateinit var conversions: ConversionsResult

    init {
        viewModelScope.launch {
            try {
                conversions = api.getConversions("USD")
                recalculateCurrencies()
            } catch (e: Exception) {
                //todo handle exception
            }
        }
    }

    private fun recalculateCurrencies(block: ((MainState) -> MainState)? = null) {
        state.update { oldState ->
            val newState = if (block != null) block(oldState) else oldState
            val amount = newState.currencySelectorState.amount.toDoubleOrNull() ?: 0.0
            val items = conversions.conversionRates.map { (currency, value) -> CurrencyItem(currency, value * amount) }
            newState.copy(currencies = items)
        }
    }

    override fun onAmountChanged(amount: String) {
        recalculateCurrencies() { currentState ->
            val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
            currentState.copy(currencySelectorState = newCurrencySelectorState)
        }
    }
}

data class MainState(val currencySelectorState: CurrencySelectorState, val currencies: List<CurrencyItem>)