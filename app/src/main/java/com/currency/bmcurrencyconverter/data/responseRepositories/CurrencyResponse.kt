package com.currency.bmcurrencyconverter.data.responseRepositories

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("rates")
    val rates: Map<String, Double>? = null,

    @SerializedName("base")
    val base: String? = null,

    @SerializedName("date")
    val date: String? = null
)
