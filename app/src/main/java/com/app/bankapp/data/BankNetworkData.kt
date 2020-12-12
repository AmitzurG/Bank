package com.app.bankapp.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


object BankNetworkData {
    private const val TAG = "BankNetworkData"
    const val apiKey = "C720AFRWCP4PJWHS"
    private const val baseUrl = "https://www.alphavantage.co"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val bankService = retrofit.create(BankService::class.java)

    suspend fun getSeriesIntraday(symbol: String, interval: String): List<StockInfo> {
        try {
            val stockInfoJsonObject = bankService.getSeriesIntraday(symbol, interval)
            val stockMetaData = stockInfoJsonObject["Meta Data"] as JsonObject
            val timeSeriesKey = "Time Series (${stockMetaData["4. Interval"].asString})"
            val stockTimeSeries = stockInfoJsonObject[timeSeriesKey]
            val timeSeriesElement = JsonParser.parseString(stockTimeSeries.toString())
            val timeSeriesJsonObject = timeSeriesElement.asJsonObject
            val seriesEntries = timeSeriesJsonObject.entrySet()
            return getSeries(seriesEntries)
        } catch (e: Exception) {
            Log.e(TAG, "error=${e.message}")
        }
        return emptyList()
    }

    private fun getSeries(seriesEntries: Set<Map.Entry<String, JsonElement>>): List<StockInfo> {
        val serieses = mutableListOf<StockInfo>()
        for (entry in seriesEntries) {
            val stockInfo = Gson().fromJson(entry.value, StockInfo::class.java)
            val time = entry.key.split(" ")[1].substring(0..4) // only time (without seconds) without date
            stockInfo.time = time
            serieses.add(stockInfo)
        }
        return serieses
    }

}

private interface BankService {
    @GET("/query?function=TIME_SERIES_INTRADAY&apikey=${BankNetworkData.apiKey}")
    suspend fun getSeriesIntraday(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String
    ): JsonObject
}