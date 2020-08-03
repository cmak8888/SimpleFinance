package com.csci448.FP.simplefinance.data.financialgoal_data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.*
import java.util.concurrent.Executors

class FinancialGoalRepository(private val fgDao: FinancialGoalDAO) {

    private val executor = Executors.newSingleThreadExecutor()

    fun getGoals(): LiveData<PagedList<FinancialGoal>> {
        return LivePagedListBuilder(
            fgDao.getGoals(),
            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
        ).build()
    }

    fun getGoal(id: UUID): LiveData<FinancialGoal?> {
        return fgDao.getGoal(id)
    }

    fun updateGoal(goal: FinancialGoal) {
        executor.execute{
            fgDao.updateGoal(goal)
        }
    }

    fun addGoal(goal: FinancialGoal) {
        executor.execute {
            fgDao.addGoal(goal)
        }
    }

    fun deleteGoal(fg: FinancialGoal) {
        executor.execute{
            fgDao.deleteGoal(fg)
        }
    }

    companion object {
        private var instance: FinancialGoalRepository? = null
        fun getInstance(context: Context): FinancialGoalRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = FinancialGoalDatabase.getInstance(context)
                    instance = FinancialGoalRepository(database.financialGoalDao())
                }
                return instance
            }
        }
    }

}