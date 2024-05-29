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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.currency.bmcurrencyconverter.viewmodels.CurrencyViewModel

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel, navController: NavHostController) {
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("USD") }
    var amount by remember { mutableStateOf("1") }
    var convertedAmount by remember { mutableStateOf("N/A") }
    val latestRates by viewModel.latestRates.observeAsState()
    var conversionRate by remember { mutableStateOf(1.0) }

    LaunchedEffect(toCurrency) {
        viewModel.fetchLatestRates("e2f2be97b38a4b3d7c2df3f641e64f2a", fromCurrency, toCurrency)
    }

    LaunchedEffect(latestRates) {
        latestRates?.let { rates ->
            val rate = rates.rates?.get(toCurrency)?.times(amount.toDoubleOrNull() ?: 1.0)
            conversionRate = rates.rates?.get(toCurrency) ?: 1.0
            convertedAmount = rate?.toString() ?: "N/A"
        }
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
            Button(
                onClick = {
                    val temp = fromCurrency
                    fromCurrency = toCurrency
                    toCurrency = temp
                    val inverseRate = 1 / conversionRate
                    amount =
                        (convertedAmount.toDoubleOrNull()?.times(inverseRate) ?: 0.0).toString()
                    convertedAmount =
                        (amount.toDoubleOrNull()?.times(conversionRate) ?: 0.0).toString()
                }
            ) {
                Text("↔")
            }
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenu(
                items = listOf(
                    "USD",
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
                onItemSelected = {
                    toCurrency = it
                    viewModel.fetchLatestRates(
                        "e2f2be97b38a4b3d7c2df3f641e64f2a",
                        fromCurrency,
                        toCurrency
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = amount,
                onValueChange = { newValue ->
                    if (newValue.isValidNumber()) {
                        amount = newValue
                        val rate = conversionRate.times(newValue.toDoubleOrNull() ?: 1.0)
                        convertedAmount = rate.toString()
                    }
                },
                label = { Text("Amount in EUR") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = convertedAmount,
                onValueChange = { newValue ->
                    if (newValue.isValidNumber()) {
                        convertedAmount = newValue
                        val inverseRate = 1 / conversionRate
                        amount = newValue.toDoubleOrNull()?.times(inverseRate)?.toString() ?: ""
                    }
                },
                label = { Text("Converted Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(150.dp)
            )
        }
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
fun DropdownMenu(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
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

private fun String.isValidNumber(): Boolean {
    return this.toDoubleOrNull() != null
}
