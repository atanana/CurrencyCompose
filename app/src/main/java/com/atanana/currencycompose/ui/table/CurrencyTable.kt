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
    state: CurrencyTableState,
    onCurrenciesListChanged: (List<Currency>) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogState by remember { mutableStateOf(false) }

    LazyColumn(contentPadding = PaddingValues(PADDING), modifier = modifier.clickable { dialogState = true }) {
        items(
            items = state.rows,
            key = { it.currency.value },
            itemContent = { CurrencyTableItem(row = it) }
        )
    }

    if (dialogState) {
        CurrenciesListSelectorDialog(state.selectorItems, onCurrenciesListChanged) {
            dialogState = false
        }
    }
}

@Composable
private fun CurrencyTableItem(row: CurrencyRow) {
    Row {
        Text(text = "%.3f".format(row.amount), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(HALF_PADDING))
        Text(text = row.currency.value)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyTablePreview() {
    CurrencyComposeTheme {
        val currencies = listOf(
            CurrencyRow(Currency("USD"), 1.0),
            CurrencyRow(Currency("RUB"), 300.0),
            CurrencyRow(Currency("BYN"), 5.0)
        )
        val state = CurrencyTableState(currencies, emptyList())
        CurrencyTable(state, {})
    }
}

@Immutable
data class CurrencyRow(val currency: Currency, val amount: Double)

@Immutable
data class CurrencyTableState(val rows: List<CurrencyRow>, val selectorItems: List<CurrencySelectorItem>)