package com.currency.bmcurrencyconverter.ui.screens

import android.graphics.Color
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.currency.bmcurrencyconverter.data.models.HistoricalData
import com.currency.bmcurrencyconverter.viewmodels.CurrencyViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoricalDataScreen(viewModel: CurrencyViewModel) {
    val historicalData by viewModel.conversionHistory.observeAsState(emptyList())
    val popularCurrencies =
        listOf("USD", "GBP", "INR", "AUD", "CAD", "CHF", "CNY", "JPY", "NZD", "BRL", "ZAR")
    val popularRates by viewModel.popularRates.observeAsState(emptyMap())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Historical Data",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (historicalData.isNotEmpty()) {
            HistoricalDataChart(historicalData)

            Spacer(modifier = Modifier.height(16.dp))

            // Using MPAndroidChart library to display prevailing currency conversions for top currencies
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Historical Data List",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    historicalData.takeLast(3).forEach { data ->
                        val date = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(Date(data.date))
                        Column {
                            Text("$date:", modifier = Modifier.padding(bottom = 4.dp))
                            data.rates.forEach { (currency, rate) ->
                                Text(
                                    "$currency: $rate",
                                    modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Popular Currencies",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    popularCurrencies.forEach { currency ->
                        val rate = popularRates[currency] ?: 0.0
                        Text(
                            "$currency: $rate",
                            modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        } else {
            Text("No historical data available")
        }
    }
}

@Composable
fun HistoricalDataChart(historicalData: List<HistoricalData>) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600
                )

                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawLabels(true)
                    setDrawAxisLine(false)
                }

                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawLabels(true)
                    setDrawAxisLine(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = XAxisDateFormatter(historicalData)
                }

                axisRight.isEnabled = false

                legend.apply {
                    form = Legend.LegendForm.LINE
                    textColor = Color.BLACK
                }

                description.isEnabled = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        update = { lineChart ->
            val entries = historicalData.takeLast(3).flatMapIndexed { index, data ->
                data.rates.map { (currency, rate) ->
                    Entry(index.toFloat(), rate.toFloat())
                }
            }

            val dataSet = LineDataSet(entries, "Exchange Rate").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                axisDependency = YAxis.AxisDependency.LEFT
            }

            lineChart.data = LineData(dataSet)
            lineChart.invalidate()
        }
    )
}

class XAxisDateFormatter(private val historicalData: List<HistoricalData>) :
    com.github.mikephil.charting.formatter.ValueFormatter() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < historicalData.size) {
            val dateInMillis = historicalData[index].date
            dateFormat.format(Date(dateInMillis))
        } else {
            ""
        }
    }
}
