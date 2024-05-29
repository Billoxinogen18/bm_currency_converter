package com.currency.bmcurrencyconverter.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.bmcurrencyconverter.data.models.HistoricalData
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
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
                    _latestRates.value = it
                    it.rates?.let { rates -> saveConversion(System.currentTimeMillis(), rates) }
                } ?: run {
                    Log.e("CurrencyViewModel", "Error fetching latest rates")
                }
            } catch (e: IOException) {
                Log.e("CurrencyViewModel", "Network error fetching latest rates", e)
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Unexpected error fetching latest rates", e)
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
        val symbols = "USD,GBP,INR,JPY,CHF,AUD,CAD,NZD,CNY,SGD"
        viewModelScope.launch {
            try {
                val response =
                    repository.getLatestRates("a69d81bc19062a3523984353e3e11a0f", base, symbols)
                response?.let {
                    it.rates?.let { rates ->
                        _popularRates.value = rates
                        savePopularRatesToPreferences(rates)
                    } ?: run {
                        Log.e("CurrencyViewModel", "Error fetching popular rates: Rates are null")
                    }
                } ?: run {
                    Log.e("CurrencyViewModel", "Error fetching popular rates")
                }
            } catch (e: IOException) {
                Log.e("CurrencyViewModel", "Network error fetching popular rates", e)
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Unexpected error fetching popular rates", e)
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
