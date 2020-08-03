package com.csci448.FP.simplefinance.data.transactions_data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


private const val DATABASE_NAME = "transactions"
@Database(entities = [Transactions::class], version = 1)
@TypeConverters(TransactionsTypeConverters::class)

abstract class TransactionsDatabase: RoomDatabase() {

    companion object {
        private var instance: TransactionsDatabase? = null
        fun getInstance(context: Context): TransactionsDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context,
                    TransactionsDatabase::class.java,
                    DATABASE_NAME).build()
            }
        }
    }

    abstract fun transactionsDao(): TransactionsDAO
}