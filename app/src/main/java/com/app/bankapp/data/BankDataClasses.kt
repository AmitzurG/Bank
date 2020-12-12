package com.app.bankapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Bank(val name: String, val stk: String, val img: String, val priority: String)

data class StockInfo(
    @Expose(serialize = false) var time: String,
    @SerializedName("1. open") val open: String,
    @SerializedName("2. high") val high: String,
    @SerializedName("3. low") val low: String,
    @SerializedName("4. close") val close: String,
    @SerializedName("5. volume") val volume: String
)