package com.app.bankapp.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bankapp.R
import com.app.bankapp.data.StockInfo
import com.app.bankapp.databinding.FragmentBankStockInfoBinding


class BankStockInfoFragment : Fragment() {

    private var binding: FragmentBankStockInfoBinding? = null
    private val args: BankStockInfoFragmentArgs by navArgs()
    private val viewModel by lazy { (activity as BankActivity).bankViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBankStockInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStockInfoRecyclerView()
        observeIntervalChange()
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "${getString(R.string.bankStockInfoLabel)} (${args.symbol})"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "(${getString(R.string.interval)} ${viewModel.intervalText})"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_stock_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionChangeInterval -> {
                ChooseIntervalFragmentDialog().show(childFragmentManager, null)
                true
            }
            R.id.actionShowGraph -> {
                val action = BankStockInfoFragmentDirections.actionBankStockInfoFragmentToBankStockGraphFragment()
                findNavController().navigate(action)
                true
            }
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        (binding?.stockInfoRecyclerView?.adapter as? StockInfoRecyclerViewAdapter)?.binding = null
    }

    private fun setStockInfoRecyclerView() = binding?.stockInfoRecyclerView?.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        addItemDecoration(dividerItemDecoration)
        adapter = StockInfoRecyclerViewAdapter()
        binding?.waitingProgressBar?.visibility = View.VISIBLE
        viewModel.getSeriesIntraday(args.symbol, viewModel.intervalValue).observe(viewLifecycleOwner, {
            showStockInformation(it)
            binding?.waitingProgressBar?.visibility = View.GONE
        })
    }

    private fun observeIntervalChange() {
        viewModel.intervalIndex.observe(viewLifecycleOwner) {
            binding?.waitingProgressBar?.visibility = View.VISIBLE
            viewModel.getSeriesIntraday(args.symbol, viewModel.intervalValue).observe(viewLifecycleOwner, {
                showStockInformation(it)
                (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "(${getString(R.string.interval)} ${viewModel.intervalText})"
                binding?.waitingProgressBar?.visibility = View.GONE
            })
        }
    }

    private fun showStockInformation(stockInfoList: List<StockInfo>) {
        if (stockInfoList.isNotEmpty()) {
            (binding?.stockInfoRecyclerView?.adapter as? StockInfoRecyclerViewAdapter)?.stockInfoSeries = stockInfoList
            gettingInformationSucceeded()
        } else {
            gettingInformationSucceeded(false)
        }
    }

    private fun gettingInformationSucceeded(successed: Boolean = true) {
        binding?.stockInfoRecyclerView?.visibility = if (successed) View.VISIBLE else View.GONE
        binding?.failedTextView?.visibility = if (successed) View.GONE else View.VISIBLE
    }

    class ChooseIntervalFragmentDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val viewModel = (activity as BankActivity).bankViewModel
            val builder: AlertDialog.Builder = AlertDialog.Builder(context).
            setTitle(R.string.chooseInterval).
            setSingleChoiceItems(R.array.intervals, viewModel.intervalIndex.value ?: 0) { _, which ->
                if (which != viewModel.intervalIndex.value) {
                    viewModel.intervalIndex.value = which
                }
                dismiss()
            }
            return builder.create()
        }
    }
}