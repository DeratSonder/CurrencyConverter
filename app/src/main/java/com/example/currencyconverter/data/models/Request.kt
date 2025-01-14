package com.example.currencyconverter.data.models

data class Request(
    val amount: Double,
    val from: String,
    val query: String,
    val to: String
)