package com.atanana.currencycompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atanana.currencycompose.MainState
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme

@Composable
fun CurrencyApp(state: MainState, actions: CurrencyAppActions) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        when (state) {
            is MainState.Data -> MainContent(state, actions)
            is MainState.Error -> Error(state)
            is MainState.Loading -> Loading()
        }
    }
}

@Composable
private fun MainContent(state: MainState.Data, actions: CurrencyAppActions) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        CurrencySelector(
            state.currencySelectorState,
            state.allCurrencies,
            actions::onAmountChanged,
            actions::onCurrencySelected
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface)
        )
        CurrencyTable(currencies = state.currencies, Modifier.fillMaxWidth())
    }
}

@Composable
private fun Error(state: MainState.Error) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = state.message, Modifier.align(Alignment.Center))
    }
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrencyAppMainContent() {
    val state = MainState.Data(
        CurrencySelectorState("123", "USD"), listOf(
            CurrencyItem("RUB", 100.0),
            CurrencyItem("BYN", 5.0)
        ), emptyList()
    )
    CurrencyComposeTheme {
        CurrencyApp(state = state, actions = object : CurrencyAppActions {
            override fun onAmountChanged(amount: String) {
            }

            override fun onCurrencySelected(currency: String) {
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrencyAppError() {
    CurrencyComposeTheme {
        Error(MainState.Error("Test error"))
    }
}

interface CurrencyAppActions {
    fun onAmountChanged(amount: String)

    fun onCurrencySelected(currency: String)
}