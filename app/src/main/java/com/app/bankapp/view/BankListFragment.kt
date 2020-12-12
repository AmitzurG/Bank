package com.app.bankapp.view

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.bankapp.R
import com.app.bankapp.databinding.FragmentBankListBinding

class BankListFragment : Fragment() {
    private var binding: FragmentBankListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBankListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBanksRecyclerView()
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.bankListLabel)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        (binding?.banksRecyclerView?.adapter as? BanksRecyclerViewAdapter)?.binding = null
    }

    private fun setBanksRecyclerView() = binding?.banksRecyclerView?.apply {
        setHasFixedSize(true)
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val bankItemWidth = resources.getDimension(R.dimen.bank_item_width)
        val columnsCount = (screenWidth / bankItemWidth).toInt() // columns number of grid according to the screen width
        layoutManager = GridLayoutManager(context, columnsCount)
        adapter = BanksRecyclerViewAdapter((activity as BankActivity).bankViewModel.bankList, screenWidth / columnsCount)
        (adapter as BanksRecyclerViewAdapter).bankItemClick = {
            val action = BankListFragmentDirections.actionBankListFragmentToBankStockInfoFragment(it)
            findNavController().navigate(action)
        }
    }
}
