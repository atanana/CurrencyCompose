package com.atanana.currencycompose

import androidx.lifecycle.ViewModel
import com.atanana.currencycompose.data.network.Api
import com.atanana.currencycompose.ui.CurrencySelectorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {

    private val state = MutableStateFlow(MainState(CurrencySelectorState("0", "USD")))
    val stateFlow: StateFlow<MainState> = state

    fun onAmountChanged(amount: String) {
        val currentState = state.value
        val newCurrencySelectorState = currentState.currencySelectorState.copy(amount = amount)
        state.value = currentState.copy(currencySelectorState = newCurrencySelectorState)
    }
}

data class MainState(val currencySelectorState: CurrencySelectorState)