package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository

class FinancialGoalFragmentViewModel(private val financialGoalRepository: FinancialGoalRepository): ViewModel() {
    val goals = financialGoalRepository.getGoals()
    val goalListLiveData: LiveData<PagedList<FinancialGoal>> = financialGoalRepository.getGoals()



    fun applyFilter(goals: List<FinancialGoal>, query: String): List<FinancialGoal> {
        val filteredGoals : MutableList<FinancialGoal> = mutableListOf()
        System.out.println("The query is $query")
        if(query.isNotEmpty()) {
            for (i in 0 until goals.size) {
                if (goals[i].title.toLowerCase().contains(query.toLowerCase())) {
                    filteredGoals.add(goals[i])
                }
            }

            return filteredGoals
        }

        else {
            return goals
        }
    }

    fun updateGoal(financialGoal: FinancialGoal) {
        financialGoalRepository.updateGoal(financialGoal)
    }



}