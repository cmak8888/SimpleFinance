package com.csci448.FP.simplefinance.ui.transactions.transactions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository

class TransactionFragmentViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return modelClass.getConstructor(TransactionsRepository::class.java).newInstance(TransactionsRepository.getInstance(context))
    }
}