package com.atanana.currencycompose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CurrencySelectorDialog(
    currencies: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(Modifier.padding(vertical = 32.dp)) {
            Card(elevation = 8.dp, shape = RoundedCornerShape(8.dp)) {
                LazyColumn(contentPadding = PaddingValues(vertical = 16.dp), modifier = Modifier.fillMaxWidth()) {
                    items(
                        items = currencies,
                        key = { it },
                        itemContent = { CurrencyItem(it, onSelect, onDismiss) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currency: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Text(text = currency,
        Modifier
            .clickable {
                onSelect(currency)
                onDismiss()
            }
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    )
}