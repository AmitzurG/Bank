package com.app.bankapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.bankapp.R
import com.app.bankapp.data.Bank
import com.app.bankapp.databinding.BankItemLayoutBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class BanksRecyclerViewAdapter(private val banks: List<Bank>, private val itemWidth: Int) : RecyclerView.Adapter<BanksRecyclerViewAdapter.BankViewHolder>() {

    class BankViewHolder(val bankViewBinding : BankItemLayoutBinding) : RecyclerView.ViewHolder(bankViewBinding.root)

    var binding: BankItemLayoutBinding? = null
    var bankItemClick: (symbol: String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        binding = BankItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding?.root?.layoutParams?.width = itemWidth
        return BankViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        setBankViewClickListener(banks[position], holder.bankViewBinding.bankItemLayout)
        setBankImage(banks[position].img, holder.bankViewBinding.bankImageView)
        holder.bankViewBinding.nameTextView.text = banks[position].name
        holder.bankViewBinding.stockNameTextView.text = banks[position].stk
        holder.bankViewBinding.priorityTextView.text = banks[position].priority
    }

    override fun getItemCount(): Int = banks.size

    private fun setBankViewClickListener(bank: Bank, bankItemView: View) = bankItemView.setOnClickListener {
        bankItemClick(bank.stk)
    }

    private fun setBankImage(imageUrl: String, imageView: ImageView) = Glide
        .with(imageView.context)
        .load(imageUrl)
        .error(R.drawable.n_a)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}