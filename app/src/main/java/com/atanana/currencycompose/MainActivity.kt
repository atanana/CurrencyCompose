package com.atanana.currencycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.atanana.currencycompose.ui.CurrencyApp
import com.atanana.currencycompose.ui.theme.CurrencyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyComposeTheme {
                CurrencyApp(mainState = viewModel.state, actions = viewModel)
            }
        }
    }
}