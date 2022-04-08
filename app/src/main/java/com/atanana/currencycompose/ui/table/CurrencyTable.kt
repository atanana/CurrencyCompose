package com.atanana.currencycompose.ui.table

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atanana.currencycompose.data.Currency
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme
import com.atanana.currencycompose.ui.theme.HALF_PADDING
import com.atanana.currencycompose.ui.theme.PADDING

@Composable
fun CurrencyTable(currencies: List<CurrencyItem>, modifier: Modifier = Modifier) {
    LazyColumn(contentPadding = PaddingValues(PADDING), modifier = modifier) {
        items(
            items = currencies,
            key = { it.currency.value },
            itemContent = { CurrencyTableItem(item = it) }
        )
    }
}

@Composable
private fun CurrencyTableItem(item: CurrencyItem) {
    Row {
        Text(text = "%.3f".format(item.amount), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(HALF_PADDING))
        Text(text = item.currency.value)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyTablePreview() {
    CurrencyComposeTheme {
        val currencies = listOf(
            CurrencyItem(Currency("USD"), 1.0),
            CurrencyItem(Currency("RUB"), 300.0),
            CurrencyItem(Currency("BYN"), 5.0)
        )
        CurrencyTable(currencies = currencies)
    }
}

@Immutable
data class CurrencyItem(val currency: Currency, val amount: Double)