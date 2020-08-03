package com.csci448.FP.simplefinance.data.transactions_data


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.*
import java.util.concurrent.Executors

class TransactionsRepository(private val transactionsDAO: TransactionsDAO) {

    private val executor = Executors.newSingleThreadExecutor()

    fun getTransactions(): LiveData<PagedList<Transactions>> {
        return LivePagedListBuilder(
            transactionsDAO.getTransactions(),
            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
        ).build()
    }

    fun getTransaction(id: UUID): LiveData<Transactions?> {
        return transactionsDAO.getTransaction(id)
    }

    fun updateTransaction(transaction: Transactions) {
        executor.execute{
            transactionsDAO.updateTransaction(transaction)
        }
    }

    fun addTransaction(transaction: Transactions) {
        executor.execute {
            transactionsDAO.addTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transactions) {
        executor.execute{
            transactionsDAO.deleteTransaction(transaction)
        }
    }

    companion object {
        private var instance: TransactionsRepository? = null
        fun getInstance(context: Context): TransactionsRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = TransactionsDatabase.getInstance(context)
                    instance = TransactionsRepository(database.transactionsDao())
                }
                return instance
            }
        }
    }


}