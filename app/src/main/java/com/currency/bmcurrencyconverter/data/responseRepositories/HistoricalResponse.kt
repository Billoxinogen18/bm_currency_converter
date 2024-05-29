package com.currency.bmcurrencyconverter.data.responseRepositories

import com.google.gson.annotations.SerializedName

data class HistoricalResponse(
    @SerializedName("rates")
    val rates: Map<String, Map<String, Double>>? = null,

    @SerializedName("base")
    val base: String? = null,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null
)
