package com.atanana.currencycompose.ui.selector

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
import com.atanana.currencycompose.domain.Currency
import com.atanana.currencycompose.ui.theme.DOUBLE_PADDING
import com.atanana.currencycompose.ui.theme.HALF_PADDING
import com.atanana.currencycompose.ui.theme.PADDING

@Composable
fun CurrencySelectorDialog(
    currencies: List<Currency>,
    onSelect: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(Modifier.padding(vertical = DOUBLE_PADDING)) {
            Card(elevation = 8.dp, shape = RoundedCornerShape(8.dp)) {
                LazyColumn(contentPadding = PaddingValues(vertical = PADDING), modifier = Modifier.fillMaxWidth()) {
                    items(
                        items = currencies,
                        key = { it.value },
                        itemContent = { CurrencyItem(it, onSelect, onDismiss) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currency: Currency,
    onSelect: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    Text(text = currency.value,
        Modifier
            .clickable {
                onSelect(currency)
                onDismiss()
            }
            .padding(vertical = HALF_PADDING, horizontal = PADDING)
            .fillMaxWidth()
    )
}