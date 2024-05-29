package com.currency.bmcurrencyconverter.data.api

import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.bmcurrencyconverter.data.responseRepositories.HistoricalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<CurrencyResponse>

    //TODO Fetch rates using timeseries(Paid)
    @GET("timeseries")
    suspend fun getHistoricalRates(
        @Query("access_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<HistoricalResponse>
}
