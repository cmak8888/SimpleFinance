package com.csci448.FP.simplefinance.ui.transactions.transactions

import androidx.lifecycle.ViewModel
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository

class TransactionFragmentViewModel(private val transactionsRepository: TransactionsRepository):
    ViewModel()  {


    val transactionListLiveData = transactionsRepository.getTransactions()
    fun addTransaction(transaction: Transactions){
        transactionsRepository.addTransaction(transaction)
    }

    fun applyTransactionFilter(transactions: List<Transactions>, query: String): List<Transactions> {
        val filteredTransactions : MutableList<Transactions> = mutableListOf()
        System.out.println("The query is $query")
        if(query.isNotEmpty()) {
            for (i in 0 until transactions.size) {
                if (transactions[i].title.toLowerCase().contains(query.toLowerCase())) {
                    filteredTransactions.add(transactions[i])
                }
            }

            return filteredTransactions
        }

        else {
            return transactions
        }
    }
}