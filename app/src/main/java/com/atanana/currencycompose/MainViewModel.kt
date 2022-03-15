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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel(), CurrencyAppActions {

    var state by mutableStateOf(MainState(CurrencySelectorState("1", "USD"), emptyList()))
        private set

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
        val newState = if (block != null) block(state) else state
        val amount = newState.currencySelectorState.amount.toDoubleOrNull() ?: 0.0
        val items = conversions.conversionRates.map { (currency, value) -> CurrencyItem(currency, value * amount) }
        state = newState.copy(currencies = items)
    }

    override fun onAmountChanged(amount: String) {
        recalculateCurrencies() { currentState ->
            val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
            currentState.copy(currencySelectorState = newCurrencySelectorState)
        }
    }
}

data class MainState(val currencySelectorState: CurrencySelectorState, val currencies: List<CurrencyItem>)