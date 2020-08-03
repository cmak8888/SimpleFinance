package com.csci448.FP.simplefinance.ui.transactions.transactions_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository
import java.util.*

class TransactionsFormViewModel(private val transactionsRepository: TransactionsRepository): ViewModel() {
    private val transactionIdLiveData =  MutableLiveData<UUID>()
    var transactionsLiveData: LiveData<Transactions?> =
        Transformations.switchMap(transactionIdLiveData){ transactionId ->
            transactionsRepository.getTransaction(transactionId)
        }
    fun loadTransaction(transactionId: UUID){
        transactionIdLiveData.value = transactionId
    }
    fun saveTransaction(transaction: Transactions){
        transactionsRepository.updateTransaction(transaction)
    }
    fun addTransaction(transaction: Transactions){
        transactionsRepository.addTransaction(transaction)
    }
}