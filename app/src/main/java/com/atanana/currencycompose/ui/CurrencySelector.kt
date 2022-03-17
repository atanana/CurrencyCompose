package com.atanana.currencycompose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atanana.currencycompose.data.Currency
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme

@Composable
fun CurrencySelector(
    state: CurrencySelectorState,
    allCurrencies: List<Currency>,
    onAmountChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit
) {
    var dialogState by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = state.amount,
            onValueChange = onAmountChanged,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { dialogState = true }) {
            Text(text = state.currency.value)
        }
    }

    if (dialogState) {
        CurrencySelectorDialog(allCurrencies, onCurrencySelected) {
            dialogState = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectorPreview() {
    CurrencyComposeTheme {
        CurrencySelector(CurrencySelectorState("0", Currency("USD")), listOf(), {}) {}
    }
}

data class CurrencySelectorState(val amount: String, val currency: Currency)