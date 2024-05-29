package com.currency.bmcurrencyconverter.viewmodels

import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CurrencyRepositoryTest {

    @Mock
    private lateinit var fixerApi: FixerApi

    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        currencyRepository = CurrencyRepository(fixerApi)
    }

    @Test
    fun `getLatestRates should return response when api call is successful`() = runBlocking {
        val response =
            Response.success(CurrencyResponse(base = "EUR", rates = mapOf("USD" to 1.12)))
        `when`(fixerApi.getLatestRates("apiKey", "EUR", "USD")).thenReturn(response)

        val result = currencyRepository.getLatestRates("apiKey", "EUR", "USD")

        assertEquals(response, result)
    }

    @Test
    fun `getLatestRates should return null when api call fails`() = runBlocking {
        `when`(
            fixerApi.getLatestRates(
                "apiKey",
                "EUR",
                "USD"
            )
        ).thenThrow(RuntimeException::class.java)

        val result = currencyRepository.getLatestRates("apiKey", "EUR", "USD")

        assertNull(result)
    }

    //TODO add more tests
}
