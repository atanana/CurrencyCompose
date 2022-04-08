package com.atanana.currencycompose.ui.table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.atanana.currencycompose.R
import com.atanana.currencycompose.data.Currency
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme
import com.atanana.currencycompose.ui.theme.DOUBLE_PADDING
import com.atanana.currencycompose.ui.theme.HALF_PADDING
import com.atanana.currencycompose.ui.theme.PADDING
import com.atanana.currencycompose.update

@Composable
fun CurrenciesListSelectorDialog(
    currencies: List<Currency>,
    onSelect: (List<Currency>) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        DialogContent(currencies, onSelect, onDismiss)
    }
}

@Composable
private fun DialogContent(
    currencies: List<Currency>,
    onSelect: (List<Currency>) -> Unit,
    onDismiss: () -> Unit
) {
    val items = remember { currencies.map { CurrencySelectedItem(it, true) }.toMutableStateList() }

    Box(Modifier.padding(vertical = DOUBLE_PADDING)) {
        Card(elevation = 8.dp, shape = RoundedCornerShape(8.dp)) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(onClick = { items.update { it.copy(isSelected = true) } }) {
                        Text(text = stringResource(R.string.select_all))
                    }
                    TextButton(onClick = { items.update { it.copy(isSelected = false) } }) {
                        Text(text = stringResource(R.string.select_none))
                    }
                }
                LazyColumn(contentPadding = PaddingValues(vertical = PADDING), modifier = Modifier.weight(1f)) {
                    items(
                        items = items.withIndex().toList(),
                        itemContent = { (i, item) ->
                            CurrencyItem(item) {
                                items[i] = item.copy(isSelected = !item.isSelected)
                            }
                        }
                    )
                }
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        val selectedCurrencies = items.filter { it.isSelected }
                            .map { it.currency }
                        onSelect(selectedCurrencies)
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(item: CurrencySelectedItem, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable { onClick() }
            .padding(vertical = HALF_PADDING, horizontal = PADDING)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.isSelected, onCheckedChange = null)
        Spacer(modifier = Modifier.size(HALF_PADDING))
        Text(text = item.currency.value)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyListSelectorDialogPreview() {
    CurrencyComposeTheme {
        val items = listOf(
            Currency("USD"),
            Currency("BYN")
        )
        DialogContent(items, onSelect = {}) {}
    }
}

@Immutable
private data class CurrencySelectedItem(val currency: Currency, val isSelected: Boolean)