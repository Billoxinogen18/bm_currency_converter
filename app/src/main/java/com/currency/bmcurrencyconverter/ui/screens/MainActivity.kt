package com.currency.bmcurrencyconverter.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currency.bmcurrencyconverter.viewmodels.CurrencyViewModel
import com.currency.currencyconverter.ui.HistoricalDataScreen
import com.currency.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                CurrencyApp(viewModel)
            }
        }
        viewModel.fetchLatestRatesForPopularCurrencies() // Calling this at entry point: This allows app to load history Data Screen with minimised latency: Minimised API usage
    }
}

@Composable
fun CurrencyApp(viewModel: CurrencyViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "currency_screen") {
        composable("currency_screen") {
            CurrencyScreen(viewModel, navController)
        }
        composable("historical_data_screen") {
            HistoricalDataScreen(viewModel)
        }
    }
}
