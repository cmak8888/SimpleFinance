package com.csci448.FP.simplefinance.ui.dashboard

import android.telephony.SmsManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel(private val financialGoalRepository: FinancialGoalRepository) : ViewModel(){


    val goalListLiveData: LiveData<PagedList<FinancialGoal>> = financialGoalRepository.getGoals()

    fun getRecentGoal(goals: List<FinancialGoal>) : FinancialGoal {
        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayYear = today.get(Calendar.YEAR)
        val todayString = "$todayMonth/$todayDay/$todayYear"
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val todayDate = sdf.parse(todayString)
        var goal = FinancialGoal()
        for(i in goals) {
            if(i.id == goal.id) continue
            val theDate = sdf.parse(i.deadline)
            if(todayDate.after(theDate)) continue
            if(goal.deadline == "") {
                goal = i
                continue
            }
            val goalDate = sdf.parse(goal.deadline)
            if(theDate == todayDate) {
                goal = i
                return goal
            } else {
                if(theDate.before(goalDate)) {
                    goal = i
                }
            }

        }

        return goal

    }
}