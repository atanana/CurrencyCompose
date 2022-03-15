package com.atanana.currencycompose.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme

@Composable
fun CurrencyTable(currencies: List<CurrencyItem>, modifier: Modifier = Modifier) {
    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = modifier) {
        items(
            items = currencies,
            key = { it.currency },
            itemContent = { CurrencyTableItem(item = it) }
        )
    }
}

@Composable
fun CurrencyTableItem(item: CurrencyItem) {
    Row {
        Text(text = item.amount.toString())
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = item.currency)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyTablePreview() {
    CurrencyComposeTheme {
        val currencies = listOf(
            CurrencyItem("USD", 1.0),
            CurrencyItem("RUB", 300.0),
            CurrencyItem("BYN", 5.0)
        )
        CurrencyTable(currencies = currencies)
    }
}

data class CurrencyItem(val currency: String, val amount: Double)