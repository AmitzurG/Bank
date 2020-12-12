package com.app.bankapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bankapp.data.StockInfo
import com.app.bankapp.databinding.StockInfoItemLayoutBinding

class StockInfoRecyclerViewAdapter : RecyclerView.Adapter<StockInfoRecyclerViewAdapter.StockInfoViewHolder>() {

    class StockInfoViewHolder(val stockInfoViewBinding : StockInfoItemLayoutBinding) : RecyclerView.ViewHolder(stockInfoViewBinding.root)

    var binding: StockInfoItemLayoutBinding? = null
    var stockInfoSeries: List<StockInfo> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockInfoViewHolder {
        binding = StockInfoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockInfoViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: StockInfoViewHolder, position: Int) {
        holder.stockInfoViewBinding.timeTextView.text = stockInfoSeries[position].time
        holder.stockInfoViewBinding.openTextView.text = stockInfoSeries[position].open
        holder.stockInfoViewBinding.highTextView.text = stockInfoSeries[position].high
        holder.stockInfoViewBinding.lowTextView.text = stockInfoSeries[position].low
        holder.stockInfoViewBinding.closeTextView.text = stockInfoSeries[position].close
        holder.stockInfoViewBinding.volumeTextView.text = stockInfoSeries[position].volume
    }

    override fun getItemCount() = stockInfoSeries.size
}