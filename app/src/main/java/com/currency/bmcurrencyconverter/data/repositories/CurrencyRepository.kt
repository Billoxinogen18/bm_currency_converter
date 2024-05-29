package com.currency.currencyconverter.dataControllers.repositories

import android.util.Log
import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.bmcurrencyconverter.data.responseRepositories.HistoricalResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val api: FixerApi) {

    suspend fun getLatestRates(apiKey: String, base: String, symbols: String): CurrencyResponse? {
        return try {
            val response = api.getLatestRates(apiKey, base, symbols)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "CurrencyRepository",
                    "Error fetching latest rates: ${response.code()} ${response.message()}"
                )
                null
            }
        } catch (e: IOException) {
            when (e) {
                is SocketTimeoutException -> Log.e(
                    "CurrencyRepository",
                    "Error fetching latest rates: Server is down or network is slow ${e.message}"
                )

                is UnknownHostException -> Log.e(
                    "CurrencyRepository",
                    "Error fetching latest rates: No internet connection ${e.message}"
                )

                else -> Log.e(
                    "CurrencyRepository",
                    "Error fetching latest rates: Network issue ${e.message}"
                )
            }
            null
        } catch (e: HttpException) {
            Log.e("CurrencyRepository", "Error fetching latest rates: API issue ${e.message()}")
            //TODO handle API specific error logging
            null
        } catch (e: Exception) {
            Log.e(
                "CurrencyRepository",
                "Error fetching latest rates: Unexpected error ${e.message}"
            )
            null
        }
    }

    //TODO fetch timeseries

    suspend fun getHistoricalRates(
        apiKey: String,
        startDate: String,
        endDate: String,
        base: String,
        symbols: String
    ): HistoricalResponse? {
        return try {
            val response = api.getHistoricalRates(apiKey, startDate, endDate, base, symbols)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "CurrencyRepository",
                    "Error fetching historical rates: ${response.code()} ${response.message()}"
                )
                null
            }
        } catch (e: IOException) {
            when (e) {
                is SocketTimeoutException -> Log.e(
                    "CurrencyRepository",
                    "Error fetching historical rates: Server is down or network is slow ${e.message}"
                )

                is UnknownHostException -> Log.e(
                    "CurrencyRepository",
                    "Error fetching historical rates: No internet connection ${e.message}"
                )

                else -> Log.e(
                    "CurrencyRepository",
                    "Error fetching historical rates: Network issue ${e.message}"
                )
            }
            null
        } catch (e: HttpException) {
            Log.e("CurrencyRepository", "Error fetching historical rates: API issue ${e.message()}")
            //TODO handle API specific error logging
            null
        } catch (e: Exception) {
            Log.e(
                "CurrencyRepository",
                "Error fetching historical rates: Unexpected error ${e.message}"
            )
            null
        }
    }
}
