package com.app.bankapp.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bankapp.R
import com.app.bankapp.databinding.FragmentBankStockGraphBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.StringBuilder


class BankStockGraphFragment : Fragment() {
    companion object {
        private const val TAG = "BankStockGraphFragment"
    }

    private var binding: FragmentBankStockGraphBinding? = null
    private val viewModel by lazy { (activity as BankActivity).bankViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBankStockGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initGraph()
        val infoEnabled = viewModel.stockGraphInfoEnabled.value
        if (infoEnabled != null) {
            setGraphData(infoEnabled)
        }
        observeGraphInfoChanges()
        setSubTitle()
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.bankStockGraphLabel)
        ChooseGraphDataFragmentDialog().show(childFragmentManager, null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_stock_graph, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionDataInfo -> {
                ChooseGraphDataFragmentDialog().show(childFragmentManager, null)
                true
            }
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initGraph() {
        val xAxis = binding?.stockInfoGraph?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return reversedSeries[value.toInt()].time
            }
        }
        binding?.stockInfoGraph?.axisLeft?.setDrawLabels(false)
        binding?.stockInfoGraph?.description?.isEnabled = false
    }

    private fun setGraphData(stockInfoEnabled: BooleanArray) {
        binding?.stockInfoGraph?.clear()
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        if (stockInfoEnabled[0]) { // open info
            dataSets.add(openSetData)
        }
        if (stockInfoEnabled[1]) { // high info
            dataSets.add(highSetData)
        }
        if (stockInfoEnabled[2]) { // low info
            dataSets.add(lowSetData)
        }
        if (stockInfoEnabled[3]) { // close info
            dataSets.add(closeSetData)
        }
        if (stockInfoEnabled[4]) { // volume info
            dataSets.add(volumeSetData)
        }
        binding?.stockInfoGraph?.data = LineData(dataSets)
    }

    private fun observeGraphInfoChanges() = viewModel.stockGraphInfoEnabled.observe(viewLifecycleOwner) {
        if (it != null) {
            setGraphData(it)
            setSubTitle()
        }
    }

    private fun setSubTitle() {
        val subTitleStringBuilder = StringBuilder()
        if (viewModel.stockGraphInfoEnabled.value?.get(0) == true) { // open info
            subTitleStringBuilder.append(getString(R.string.open)).append(", ")
        }
        if (viewModel.stockGraphInfoEnabled.value?.get(1) == true) { // high info
            subTitleStringBuilder.append(getString(R.string.high)).append(", ")
        }
        if (viewModel.stockGraphInfoEnabled.value?.get(2) == true) { // low info
            subTitleStringBuilder.append(getString(R.string.low)).append(", ")
        }
        if (viewModel.stockGraphInfoEnabled.value?.get(3) == true) { // close info
            subTitleStringBuilder.append(getString(R.string.close)).append(", ")
        }
        if (viewModel.stockGraphInfoEnabled.value?.get(4) == true) { // volume info
            subTitleStringBuilder.append(getString(R.string.volume)).append(", ")
        }
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "(${subTitleStringBuilder.removeSuffix(", ")})"
    }

    private val reversedSeries by lazy { viewModel.currentSeries.reversed() }
    private val openSetData by lazy {
        val values = mutableListOf<Entry>()
        try {
            for ((i, series) in reversedSeries.withIndex()) {
                values.add(Entry(i.toFloat(), series.open.toFloat()))
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "NumberFormatException - error=${e.message}")
        }
        val set = LineDataSet(values, getString(R.string.open))
        set.color = Color.GREEN
        set.setCircleColor(Color.GREEN)
        set
    }

    private val highSetData by lazy {
        val values = mutableListOf<Entry>()
        try {
            for ((i, series) in reversedSeries.withIndex()) {
                values.add(Entry(i.toFloat(), series.high.toFloat()))
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "NumberFormatException - error=${e.message}")
        }
        val set = LineDataSet(values, getString(R.string.high))
        set.color = Color.BLUE
        set.setCircleColor(Color.BLUE)
        set
    }

    private val lowSetData by lazy {
        val values = mutableListOf<Entry>()
        try {
            for ((i, series) in reversedSeries.withIndex()) {
                values.add(Entry(i.toFloat(), series.low.toFloat()))
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "NumberFormatException - error=${e.message}")
        }
        val set = LineDataSet(values, getString(R.string.low))
        set.color = Color.CYAN
        set.setCircleColor(Color.CYAN)
        set
    }

    private val closeSetData by lazy {
        val values = mutableListOf<Entry>()
        try {
            for ((i, series) in reversedSeries.withIndex()) {
                values.add(Entry(i.toFloat(), series.close.toFloat()))
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "NumberFormatException - error=${e.message}")
        }
        val set = LineDataSet(values, getString(R.string.close))
        set.color = Color.MAGENTA
        set.setCircleColor(Color.MAGENTA)
        set
    }

    private val volumeSetData by lazy {
        val values = mutableListOf<Entry>()
        try {
            for ((i, series) in reversedSeries.withIndex()) {
                values.add(Entry(i.toFloat(), series.volume.toFloat()))
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "NumberFormatException - error=${e.message}")
        }
        val set = LineDataSet(values, getString(R.string.volume))
        set.color = Color.RED
        set.setCircleColor(Color.RED)
        set
    }

    class ChooseGraphDataFragmentDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val viewModel = (activity as BankActivity).bankViewModel
            val builder: AlertDialog.Builder = AlertDialog.Builder(context).
            setTitle(R.string.chooseGraphData).setMultiChoiceItems(
                    arrayOf(getString(R.string.open), getString(R.string.high), getString(R.string.low), getString(R.string.close), getString(R.string.volume)),
                    viewModel.stockGraphInfoEnabled.value)
            { _, which, isChecked ->
                viewModel.stockGraphInfoEnabled.value?.set(which, isChecked)
            }.
            setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.stockGraphInfoEnabled.value = viewModel.stockGraphInfoEnabled.value // triggers the observer
                dismiss()
            }.
            setNegativeButton(android.R.string.cancel) { _, _ ->
                dismiss()
            }
            return builder.create()
        }
    }

}