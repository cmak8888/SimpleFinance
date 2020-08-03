package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository
import java.util.*

class FinancialGoalFormFragmentViewModel(private val fgRepository: FinancialGoalRepository): ViewModel() {

    private val goalIdLiveData = MutableLiveData<UUID>()

    fun addGoal(financialGoal: FinancialGoal) {
        fgRepository.addGoal(financialGoal)
    }


}