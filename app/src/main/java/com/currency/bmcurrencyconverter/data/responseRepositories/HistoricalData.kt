package com.currency.bmcurrencyconverter.data.responseRepositories

data class HistoricalData(
    val date: Long,
    val rates: Map<String, Double>
)
