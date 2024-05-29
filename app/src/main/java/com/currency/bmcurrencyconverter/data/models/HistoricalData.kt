package com.currency.bmcurrencyconverter.data.models

data class HistoricalData(
    val date: Long,
    val rates: Map<String, Double>
)

