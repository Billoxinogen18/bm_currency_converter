package com.currency.bmcurrencyconverter.data.repositories


import com.currency.bmcurrencyconverter.data.netbean.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.bmcurrencyconverter.data.responseRepositories.HistoricalResponse
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val api: FixerApi) {
    suspend fun getLatestRates(
        apiKey: String,
        base: String,
        symbols: String
    ): Response<CurrencyResponse>? {
        return try {
            api.getLatestRates(apiKey, base, symbols)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getHistoricalRates(
        apiKey: String,
        startDate: String,
        endDate: String,
        base: String,
        symbols: String
    ): Response<HistoricalResponse>? {
        return try {
            api.getHistoricalRates(apiKey, startDate, endDate, base, symbols)
        } catch (e: Exception) {
            null
        }
    }
}
