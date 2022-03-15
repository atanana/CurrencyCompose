package com.atanana.currencycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.atanana.currencycompose.ui.CurrencySelector
import com.atanana.currencycompose.ui.CurrencyTable
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val state = viewModel.stateFlow.collectAsState()
                    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        CurrencySelector(state.value.currencySelectorState, viewModel::onAmountChanged)
                        CurrencyTable(currencies = state.value.currencies)
                    }
                }
            }
        }
    }
}