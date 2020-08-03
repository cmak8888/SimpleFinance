package com.csci448.FP.simplefinance.data.transactions_data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*

@Dao
interface TransactionsDAO {

    @Query("SELECT * FROM transactions")
    fun getTransactions(): DataSource.Factory<Int, Transactions>

    @Query("SELECT * FROM transactions WHERE id=(:id)")
    fun getTransaction(id: UUID): LiveData<Transactions?>

    @Update
    fun updateTransaction(transaction: Transactions)

    @Insert
    fun addTransaction(transaction: Transactions)

    @Delete
    fun deleteTransaction(transaction: Transactions)
}