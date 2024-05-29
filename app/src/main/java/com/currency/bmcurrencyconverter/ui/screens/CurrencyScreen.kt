package com.currency.bmcurrencyconverter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.currency.bmcurrencyconverter.viewmodels.CurrencyViewModel

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel, navController: NavHostController) {
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("USD") }
    var amount by remember { mutableStateOf("1") }
    val latestRates by viewModel.latestRates.observeAsState()

    LaunchedEffect(toCurrency) {
        viewModel.fetchLatestRates("a69d81bc19062a3523984353e3e11a0f", fromCurrency, toCurrency)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            DropdownMenu(
                items = listOf("EUR"),
                selectedItem = fromCurrency,
                onItemSelected = { fromCurrency = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("↔", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenu(
                items = listOf(
                    "USD",
                    "EUR",
                    "GBP",
                    "INR",
                    "CAD",
                    "AUD",
                    "CHF",
                    "JPY",
                    "CNY",
                    "NZD"
                ),
                selectedItem = toCurrency,
                onItemSelected = { toCurrency = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount in EUR") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        latestRates?.let { rates ->
            val rate = rates.rates?.get(toCurrency)?.times(amount.toDouble())
            Text(
                "Converted Amount: ${rate ?: "N/A"}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } ?: Text("Converted Amount: N/A", modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("historical_data_screen") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Details")
        }
    }
}

@Composable
fun DropdownMenu(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedItem)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}
