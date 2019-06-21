package com.example.exchangerates.data

data class Mark(
    val date: String,
    val inCur: String,
    val inValue: Int,
    val outCur: String,
    val outValue: Float
)