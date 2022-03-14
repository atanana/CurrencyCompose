package com.atanana.currencycompose

import androidx.lifecycle.ViewModel
import com.atanana.currencycompose.data.network.Api
import javax.inject.Inject

class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {
}