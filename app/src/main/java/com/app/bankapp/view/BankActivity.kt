package com.app.bankapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.bankapp.R
import com.app.bankapp.viewmodel.BankViewModel

class BankActivity : AppCompatActivity() {

    val bankViewModel by viewModels<BankViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}