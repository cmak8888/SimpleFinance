package com.csci448.FP.simplefinance.data.financialgoal_data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*

@Dao
interface FinancialGoalDAO {

    @Query("SELECT * FROM financialgoal")
    fun getGoals(): DataSource.Factory<Int, FinancialGoal>

    @Query("SELECT * FROM financialgoal WHERE id=(:id)")
    fun getGoal(id: UUID): LiveData<FinancialGoal?>

    @Update
    fun updateGoal(fg: FinancialGoal)

    @Insert
    fun addGoal(fg: FinancialGoal)

    @Delete
    fun deleteGoal(fg: FinancialGoal)

}