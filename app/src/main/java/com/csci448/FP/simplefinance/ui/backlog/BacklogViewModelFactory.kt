package com.csci448.FP.simplefinance.ui.backlog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository

class BacklogViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FinancialGoalRepository::class.java, TransactionsRepository::class.java)
            .newInstance(FinancialGoalRepository.getInstance(context), TransactionsRepository.getInstance(context))
    }
}