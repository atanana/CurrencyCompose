package com.atanana.currencycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        CurrencySelector(state.value.currencySelectorState, viewModel::onAmountChanged)
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.onSurface)
                        )
                        CurrencyTable(currencies = state.value.currencies, Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}