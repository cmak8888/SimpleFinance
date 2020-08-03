package com.csci448.FP.simplefinance.data.credentials_data

import android.content.Context
import androidx.room.*

private const val DATABASE_NAME = "account-credentials"
@Database(entities = [AccountCredentials::class], version = 1)
@TypeConverters(AccountCredentialsTypeConverters::class)

abstract class AccountCredentialsDatabase: RoomDatabase() {
    companion object {
        private var instance: AccountCredentialsDatabase? = null
        fun getInstance(context: Context): AccountCredentialsDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context,
                    AccountCredentialsDatabase::class.java,
                    DATABASE_NAME).build()
            }
        }
    }

    abstract fun accountCredentialsDao(): AccountCredentialsDAO
}