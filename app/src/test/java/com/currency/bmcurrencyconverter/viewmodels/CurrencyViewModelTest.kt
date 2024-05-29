package com.currency.bmcurrencyconverter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.currency.bmcurrencyconverter.MockSharedPreferences
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CurrencyRepository

    @Mock
    private lateinit var latestRatesObserver: Observer<CurrencyResponse?>

    @Mock
    private lateinit var popularRatesObserver: Observer<Map<String, Double>>

    private lateinit var sharedPreferences: MockSharedPreferences
    private lateinit var viewModel: CurrencyViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sharedPreferences = MockSharedPreferences()
        viewModel = CurrencyViewModel(repository, sharedPreferences)
    }

    @Test
    fun `fetchLatestRates should post value when data is fetched successfully`() = runTest {
        val response = CurrencyResponse(mapOf("USD" to 1.2))
        whenever(repository.getLatestRates("apiKey", "EUR", "USD")).thenReturn(response)

        viewModel.latestRates.observeForever(latestRatesObserver)
        viewModel.fetchLatestRates("apiKey", "EUR", "USD")

        verify(latestRatesObserver).onChanged(response)
    }

    @Test
    fun `fetchLatestRatesForPopularCurrencies should post value when data is fetched successfully`() =
        runTest {
            val response = CurrencyResponse(mapOf("USD" to 1.2, "GBP" to 0.85, "INR" to 90.0))
            whenever(repository.getLatestRates("apiKey", "EUR", "USD,GBP,INR")).thenReturn(response)

            viewModel.popularRates.observeForever(popularRatesObserver)
            viewModel.fetchLatestRatesForPopularCurrencies()

            verify(popularRatesObserver).onChanged(response.rates!!)
        }

    @Test
    fun `getPopularRate should return rate from sharedPreferences`() {
        sharedPreferences.edit().putFloat("USD", 1.2f).apply()

        val rate = viewModel.getPopularRate("USD")

        assert(rate == 1.2f)
    }

    @Test
    fun `fetchLatestRates should handle IOException`() = runTest {
        whenever(
            repository.getLatestRates(
                "apiKey",
                "EUR",
                "USD"
            )
        ).thenThrow(IOException::class.java)

        viewModel.latestRates.observeForever(latestRatesObserver)
        viewModel.fetchLatestRates("apiKey", "EUR", "USD")

        verify(latestRatesObserver).onChanged(CurrencyResponse(emptyMap()))
    }

    @Test
    fun `fetchLatestRates should handle HttpException`() = runTest {
        whenever(
            repository.getLatestRates(
                "apiKey",
                "EUR",
                "USD"
            )
        ).thenThrow(RuntimeException::class.java)

        viewModel.latestRates.observeForever(latestRatesObserver)
        viewModel.fetchLatestRates("apiKey", "EUR", "USD")

        verify(latestRatesObserver).onChanged(CurrencyResponse(emptyMap()))
    }
}
