package com.csci448.FP.simplefinance.ui.finance_goal.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository
import java.util.*

class FinancialGoalDetailFragmentViewModel(private val fgRepository: FinancialGoalRepository): ViewModel() {

    val goalIdLiveData = MutableLiveData<UUID>()

    var goalLiveData: LiveData<FinancialGoal?> =
        Transformations.switchMap(goalIdLiveData) { goalId -> fgRepository.getGoal(goalId)}

    fun loadGoal(id: UUID) {
        goalIdLiveData.value = id
    }


    fun updateGoal(financialGoal: FinancialGoal) {
        fgRepository.updateGoal(financialGoal)
    }

    fun deleteGoal(financialGoal: FinancialGoal) {
        fgRepository.deleteGoal(financialGoal)
    }

}