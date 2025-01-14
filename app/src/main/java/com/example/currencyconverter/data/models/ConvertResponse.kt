package com.example.currencyconverter.data.models

data class ConvertResponse(
    val disclaimer: String,
    val license: String,
    val meta: Meta,
    val request: Request,
    val response: Double
)