package com.currency.bmcurrencyconverter.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.bmcurrencyconverter.data.models.HistoricalData
import com.currency.bmcurrencyconverter.data.repositories.CurrencyRepository
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _latestRates = MutableLiveData<CurrencyResponse>()
    val latestRates: LiveData<CurrencyResponse> get() = _latestRates

    private val _conversionHistory = MutableLiveData<List<HistoricalData>>(emptyList())
    val conversionHistory: LiveData<List<HistoricalData>> get() = _conversionHistory

    private val _popularRates = MutableLiveData<Map<String, Double>>()
    val popularRates: LiveData<Map<String, Double>> get() = _popularRates

    init {
        fetchLatestRatesForPopularCurrencies()
    }

    fun fetchLatestRates(apiKey: String, base: String, symbols: String) {
        viewModelScope.launch {
            try {
                val response = repository.getLatestRates(apiKey, base, symbols)
                response?.let {
                    if (it.isSuccessful) {
                        val body = it.body()
                        body?.let { responseBody ->
                            _latestRates.value = responseBody
                            responseBody.rates?.let { rates ->
                                saveConversion(
                                    System.currentTimeMillis(),
                                    rates
                                )
                            }
                        } ?: run {
                            Log.e("CurrencyViewModel", "Response body is null")
                        }
                    } else {
                        Log.e(
                            "CurrencyViewModel",
                            "Error fetching latest rates: ${it.errorBody()?.string()}"
                        )
                    }
                } ?: run {
                    Log.e("CurrencyViewModel", "Response is null")
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Exception fetching latest rates", e)
            }
        }
    }

    private fun saveConversion(date: Long, rates: Map<String, Double>) {
        val newHistory = _conversionHistory.value.orEmpty().toMutableList()
        newHistory.add(HistoricalData(date, rates))
        _conversionHistory.value = newHistory
    }

    fun fetchLatestRatesForPopularCurrencies() {
        val base = "EUR"
        val symbols = "USD,GBP,INR,CAD,AUD,CHF,CNY,JPY,SEK,NZD"
        viewModelScope.launch {
            try {
                val response =
                    repository.getLatestRates("a69d81bc19062a3523984353e3e11a0f", base, symbols)
                response?.let {
                    if (it.isSuccessful) {
                        val body = it.body()
                        body?.let { responseBody ->
                            responseBody.rates?.let { rates ->
                                _popularRates.value = rates
                                savePopularRatesToPreferences(rates)
                            } ?: run {
                                Log.e("CurrencyViewModel", "Rates are null in response body")
                            }
                        } ?: run {
                            Log.e("CurrencyViewModel", "Response body is null")
                        }
                    } else {
                        Log.e(
                            "CurrencyViewModel",
                            "Error fetching popular rates: ${it.errorBody()?.string()}"
                        )
                    }
                } ?: run {
                    Log.e("CurrencyViewModel", "Response is null")
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Exception fetching popular rates", e)
            }
        }
    }

    private fun savePopularRatesToPreferences(rates: Map<String, Double>) {
        with(sharedPreferences.edit()) {
            rates.forEach { (currency, rate) ->
                putFloat(currency, rate.toFloat())
            }
            apply()
        }
    }

    fun getPopularRate(currency: String): Float {
        return sharedPreferences.getFloat(currency, 0f)
    }
}
