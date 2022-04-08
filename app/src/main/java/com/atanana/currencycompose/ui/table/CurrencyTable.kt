package com.atanana.currencycompose.ui.table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.atanana.currencycompose.data.Currency
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme
import com.atanana.currencycompose.ui.theme.HALF_PADDING
import com.atanana.currencycompose.ui.theme.PADDING

@Composable
fun CurrencyTable(
    currencies: List<CurrencyItem>,
    allCurrencies: List<Currency>,
    onCurrenciesListChanged: (List<Currency>) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogState by remember { mutableStateOf(false) }

    LazyColumn(contentPadding = PaddingValues(PADDING), modifier = modifier.clickable { dialogState = true }) {
        items(
            items = currencies,
            key = { it.currency.value },
            itemContent = { CurrencyTableItem(item = it) }
        )
    }

    if (dialogState) {
        CurrenciesListSelectorDialog(allCurrencies, onCurrenciesListChanged) {
            dialogState = false
        }
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
        CurrencyTable(currencies = currencies, emptyList(), {})
    }
}

@Immutable
data class CurrencyItem(val currency: Currency, val amount: Double)