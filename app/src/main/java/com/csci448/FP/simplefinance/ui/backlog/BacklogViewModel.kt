package com.csci448.FP.simplefinance.ui.backlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoalRepository
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.csci448.FP.simplefinance.data.transactions_data.TransactionsRepository
import java.text.SimpleDateFormat
import java.util.*

class BacklogViewModel(private val financialGoalRepository: FinancialGoalRepository, private val transactionsRepository: TransactionsRepository): ViewModel() {


    val goalListLiveData = financialGoalRepository.getGoals()
    val transactionListLiveData = transactionsRepository.getTransactions()

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

    //List of 3 transactions
    fun getMostRecentTransactions(trans: List<Transactions>): List<Transactions> {
        val recentList : MutableList<Transactions> = mutableListOf()
        recentList.add(Transactions())
        recentList.add(Transactions())
        recentList.add(Transactions())
        val today = Date()
        for(i in trans) {
            if(i.id == recentList[0].id) continue
            if(recentList[0].title == "") {
                recentList[0] = i
                continue
            } else if(i.timestamp == today){
                recentList[0] = i
                break;
            } else if(i.timestamp.after(recentList[0].timestamp)) {
                recentList[0] = i
            }
        }
        for(i in trans) {
            if(i == recentList[0] || i == recentList[1] ) continue
            if(recentList[1].title == "") {
                recentList[1] = i
                continue
            } else if(i.timestamp == today){
                recentList[1] = i
                break;
            } else if(i.timestamp.after(recentList[1].timestamp)) {
                recentList[1] = i
            }
        }
        for(i in trans) {
            if(i == recentList[0] || i == recentList[1] || i == recentList[2]) continue
            if(recentList[2].title == "") {
                recentList[2] = i
                continue
            } else if(i.timestamp == today){
                recentList[2] = i
                break;
            } else if(i.timestamp.after(recentList[2].timestamp)) {
                recentList[2] = i
            }
        }
        return recentList
    }



}