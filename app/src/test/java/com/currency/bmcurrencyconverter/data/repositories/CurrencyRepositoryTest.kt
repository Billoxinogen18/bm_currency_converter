package com.currency.bmcurrencyconverter.data.repositories

import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryTest {

    @Mock
    private lateinit var api: FixerApi

    private lateinit var repository: CurrencyRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = CurrencyRepository(api)
    }

    @Test
    fun `getLatestRates should return response when api call is successful`() = runTest {
        val response = Response.success(CurrencyResponse(mapOf("USD" to 1.2)))
        `when`(api.getLatestRates("apiKey", "EUR", "USD")).thenReturn(response)

        val result = repository.getLatestRates("apiKey", "EUR", "USD")
        assertNotNull(result)
        assertEquals(1.2, result?.rates?.get("USD"))
    }

    @Test
    fun `getLatestRates should return null when api call fails`() = runTest {
        val response = Response.error<CurrencyResponse>(400, "error".toResponseBody(null))
        `when`(api.getLatestRates("apiKey", "EUR", "USD")).thenReturn(response)

        val result = repository.getLatestRates("apiKey", "EUR", "USD")
        assertNull(result)
    }

    @Test
    fun `getLatestRates should handle IOException`() = runTest {
        `when`(api.getLatestRates("apiKey", "EUR", "USD")).thenThrow(IOException::class.java)

        val result = repository.getLatestRates("apiKey", "EUR", "USD")
        assertNull(result)
    }

    @Test
    fun `getLatestRates should handle SocketTimeoutException`() = runTest {
        `when`(
            api.getLatestRates(
                "apiKey",
                "EUR",
                "USD"
            )
        ).thenThrow(SocketTimeoutException::class.java)

        val result = repository.getLatestRates("apiKey", "EUR", "USD")
        assertNull(result)
    }
}
