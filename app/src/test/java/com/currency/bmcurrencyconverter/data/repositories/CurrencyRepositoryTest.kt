package com.currency.bmcurrencyconverter.data.repositories


import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
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
    fun `test getLatestRates success`() = runBlockingTest {
        val response = Response.success(mock(CurrencyResponse::class.java))
        `when`(api.getLatestRates(anyString(), anyString(), anyString())).thenReturn(response)

        val result = repository.getLatestRates("api_key", "EUR", "USD")

        Assert.assertEquals(response.body(), result)
    }

    @Test
    fun `test getLatestRates failure`() = runBlockingTest {
        val response = Response.error<CurrencyResponse>(404, mock(ResponseBody::class.java))
        `when`(api.getLatestRates(anyString(), anyString(), anyString())).thenReturn(response)

        val result = repository.getLatestRates("api_key", "EUR", "USD")

        Assert.assertNull(result)
    }
}
