package com.app.bankapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.app.bankapp.R
import com.app.bankapp.data.Bank
import com.app.bankapp.data.BankNetworkData
import com.app.bankapp.data.StockInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class BankViewModel(application: Application) : AndroidViewModel(application) {

    val bankList: List<Bank> by lazy {
        Gson().fromJson(jsonStringFromAssets(), object : TypeToken<List<Bank>>() {}.type)
    }

    var currentSeries: List<StockInfo> = emptyList()
        private set
    var intervalIndex = MutableLiveData<Int>()
    val intervalValue: String
        get() = getApplication<Application>().resources.getStringArray(R.array.intervalValues)[intervalIndex.value ?: 0]
    val intervalText: String
        get() = getApplication<Application>().resources.getStringArray(R.array.intervals)[intervalIndex.value ?: 0]
    val stockGraphInfoEnabled by lazy { // represent the data that the graph shows (open, high, low, close, volume),
                                        // by default only the open info is shown. this is observable property
        val graphInfoEnableLiveData = MutableLiveData<BooleanArray>()
        graphInfoEnableLiveData.value = BooleanArray(5) {
            when (it) {
                0 -> true
                else -> false
            }
        }
        graphInfoEnableLiveData
    }

    fun getSeriesIntraday(symbol: String, interval: String =  "1min") = liveData(Dispatchers.IO) {
        val series = BankNetworkData.getSeriesIntraday(symbol, interval)
        currentSeries = series
        emit(series)
    }

    private fun jsonStringFromAssets(): String {
        var jsonString = ""
        try {
            val banksInputStream = getApplication<Application>().assets.open("banks.json")
            val size = banksInputStream.available()
            val buffer = ByteArray(size)
            banksInputStream.read(buffer)
            banksInputStream.close()
            jsonString = String(buffer)
        } catch (e: IOException) {
        }
        return jsonString
    }
}

class BankViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BankViewModel(application) as T
    }
}