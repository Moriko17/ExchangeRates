package com.example.exchangerates.data

import com.example.exchangerates.data.EUR
import com.example.exchangerates.data.JPY
import com.example.exchangerates.data.USD

data class Valute(
    val USD: USD,
    val EUR: EUR?,
    val JPY: JPY
)