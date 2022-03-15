package com.atanana.currencycompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atanana.currencycompose.MainState
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme

@Composable
fun CurrencyApp(mainState: MainState, actions: CurrencyAppActions) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            CurrencySelector(mainState.currencySelectorState, actions::onAmountChanged)
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onSurface)
            )
            CurrencyTable(currencies = mainState.currencies, Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrencyApp() {
    val state = MainState(
        CurrencySelectorState("123", "USD"), listOf(
            CurrencyItem("RUB", 100.0),
            CurrencyItem("BYN", 5.0)
        )
    )
    CurrencyComposeTheme {
        CurrencyApp(mainState = state, actions = object : CurrencyAppActions {
            override fun onAmountChanged(amount: String) {
            }
        })
    }
}

interface CurrencyAppActions {
    fun onAmountChanged(amount: String)
}