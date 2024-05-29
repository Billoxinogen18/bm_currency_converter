package com.currency.bmcurrencyconverter.data.repositories

import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CurrencyRepositoryTest {

    @Mock
    private lateinit var api: FixerApi

    private lateinit var repository: CurrencyRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CurrencyRepository(api)
    }

    @Test
    fun `getLatestRates should return response when api call is successful`() = runBlocking {
        val expectedResponse = CurrencyResponse(rates = mapOf("USD" to 1.2, "GBP" to 0.8))
        Mockito.`when`(api.getLatestRates("api_key", "EUR", "USD,GBP"))
            .thenReturn(Response.success(expectedResponse))

        val actualResponse = repository.getLatestRates("api_key", "EUR", "USD,GBP")

        assert(actualResponse == expectedResponse)
    }

    @Test
    fun `getLatestRates should return null when api call fails`() = runBlocking {
        val errorResponse = Response.error<CurrencyResponse>(500, "".toResponseBody(null))

        Mockito.`when`(api.getLatestRates("api_key", "EUR", "USD,GBP"))
            .thenReturn(errorResponse)

        val actualResponse = repository.getLatestRates("api_key", "EUR", "USD,GBP")

        assert(actualResponse == null)
    }

    @Test
    fun `getLatestRates should handle IOException`() = runBlocking {
        Mockito.`when`(api.getLatestRates("api_key", "EUR", "USD,GBP"))
            .thenThrow(IOException("Network error"))

        val actualResponse = repository.getLatestRates("api_key", "EUR", "USD,GBP")

        assert(actualResponse == null)
    }

    @Test
    fun `getLatestRates should handle HttpException`() = runBlocking {
        Mockito.`when`(api.getLatestRates("api_key", "EUR", "USD,GBP"))
            .thenThrow(
                HttpException(
                    Response.error<CurrencyResponse>(
                        400,
                        "".toResponseBody(null)
                    )
                )
            )

        val actualResponse = repository.getLatestRates("api_key", "EUR", "USD,GBP")

        assert(actualResponse == null)
    }
}
