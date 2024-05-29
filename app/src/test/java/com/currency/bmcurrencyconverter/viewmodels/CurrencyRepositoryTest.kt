import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.currency.bmcurrencyconverter.data.models.HistoricalData
import com.currency.bmcurrencyconverter.data.responseRepositories.CurrencyResponse
import com.currency.bmcurrencyconverter.viewmodels.CurrencyViewModel
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    private lateinit var viewModel: CurrencyViewModel

    @Mock
    private lateinit var repository: CurrencyRepository

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var latestRatesObserver: Observer<CurrencyResponse>

    @Mock
    private lateinit var conversionHistoryObserver: Observer<List<HistoricalData>>

    @Mock
    private lateinit var popularRatesObserver: Observer<Map<String, Double>>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = CurrencyViewModel(repository, sharedPreferences)
        viewModel.latestRates.observeForever(latestRatesObserver)
        viewModel.conversionHistory.observeForever(conversionHistoryObserver)
        viewModel.popularRates.observeForever(popularRatesObserver)
    }

    @Test
    fun `fetchLatestRates should post value when data is fetched successfully`() =
        testDispatcher.runBlockingTest {
            val expectedResponse = CurrencyResponse(
                base = "EUR",
                date = "2021-01-01",
                rates = mapOf("USD" to 1.2, "GBP" to 0.9)
            )

            Mockito.`when`(repository.getLatestRates("apiKey", "EUR", "USD,GBP"))
                .thenReturn(expectedResponse)

            viewModel.fetchLatestRates("apiKey", "EUR", "USD,GBP")

            Mockito.verify(latestRatesObserver).onChanged(expectedResponse)
            Mockito.verify(conversionHistoryObserver).onChanged(Mockito.anyList())
    }

    @Test
    fun `fetchLatestRatesForPopularCurrencies should post value when data is fetched successfully`() =
        testDispatcher.runBlockingTest {
            val expectedResponse = CurrencyResponse(
                base = "EUR",
                date = "2021-01-01",
                rates = mapOf("USD" to 1.2, "GBP" to 0.9, "INR" to 88.0)
            )

            Mockito.`when`(
                repository.getLatestRates(
                    "e2f2be97b38a4b3d7c2df3f641e64f2a",
                    "EUR",
                    "USD,GBP,INR"
                )
            )
                .thenReturn(expectedResponse)

            viewModel.fetchLatestRatesForPopularCurrencies()

            Mockito.verify(popularRatesObserver).onChanged(expectedResponse.rates!!)
    }

    @Test
    fun `getPopularRate should return rate from sharedPreferences`() {
        Mockito.`when`(sharedPreferences.getFloat("USD", 0f)).thenReturn(1.2f)

        val rate = viewModel.getPopularRate("USD")

        assert(rate == 1.2f)
    }
}
