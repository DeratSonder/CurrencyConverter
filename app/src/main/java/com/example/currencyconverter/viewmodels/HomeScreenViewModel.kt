package com.example.currencyconverter.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.ConvertResponse
import com.example.currencyconverter.data.repositories.CurrencyConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository
): ViewModel() {

    private val _currencies = MutableStateFlow<Map<String, String>>(emptyMap())
    val currencies: MutableStateFlow<Map<String, String>> = _currencies

    private val _convertedValue = MutableStateFlow<Double>(0.0)
    val convertedValue: MutableStateFlow<Double> = _convertedValue

    init {
       // getCurrencies()
    }

    private fun getCurrencies() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getCurrencies()
            }
            Log.d("CurrencyConverter", "getCurrencies: $response")
            if (response.isSuccess) {
                _currencies.value = response.getOrDefault(emptyMap())
            } else {
                _currencies.value = emptyMap()
            }
        }
    }

    fun convert(value: Double, fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.convert(value, fromCurrency, toCurrency)
            }
            if (response.isSuccess) {
                val result = response.getOrDefault(0.0) as ConvertResponse
                _convertedValue.value = result.response
                Log.d("CurrencyConverter", "convertCurrency: $result")
            } else {
                Log.d("CurrencyConverter", "convertCurrency: Error")
                _convertedValue.value = 0.0
            }
        }
    }

}