package com.example.currencyconverter.data.models

data class ConvertError(
    val description: String,
    val error: Boolean,
    val message: String,
    val status: Int
)