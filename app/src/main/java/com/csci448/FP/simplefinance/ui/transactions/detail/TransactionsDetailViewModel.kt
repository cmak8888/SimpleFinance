package com.csci448.FP.simplefinance.ui.transactions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository
import java.util.*

class TransactionsDetailViewModel(private val transactionsRepository: TransactionsRepository): ViewModel() {
    private val transactionsIdLiveData =  MutableLiveData<UUID>()
    var transactionsLiveData: LiveData<Transactions?> =
        Transformations.switchMap(transactionsIdLiveData){ transactionId ->
            transactionsRepository.getTransaction(transactionId)
        }
    fun loadTransaction(transactionId: UUID){
        transactionsIdLiveData.value = transactionId
    }
    fun saveTransaction(transactions: Transactions){
        transactionsRepository.updateTransaction(transactions)
    }
    fun deleteTransaction(transactions: Transactions){
        transactionsRepository.deleteTransaction(transactions)
    }
}