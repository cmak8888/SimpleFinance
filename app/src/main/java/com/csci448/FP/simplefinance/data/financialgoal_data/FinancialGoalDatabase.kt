package com.csci448.FP.simplefinance.data.financialgoal_data

import android.content.Context
import androidx.room.*
import java.security.AccessControlContext


private const val DATABASE_NAME = "finance-goals"
@Database(entities = [FinancialGoal::class], version = 1)
@TypeConverters(FinancialGoalTypeConverters::class)

abstract class FinancialGoalDatabase: RoomDatabase() {
    companion object {
        private var instance: FinancialGoalDatabase? = null
        fun getInstance(context: Context): FinancialGoalDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context,
                    FinancialGoalDatabase::class.java,
                    DATABASE_NAME).build()
            }
        }
    }

    abstract fun financialGoalDao(): FinancialGoalDAO
}