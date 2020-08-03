package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import androidx.recyclerview.widget.DiffUtil
import com.csci448.FP.simplefinance.R


class FinancialGoalAdapter(private val goals: List<FinancialGoal>, private val clickListener: (FinancialGoal) -> Unit): PagedListAdapter<FinancialGoal, FinancialGoalHolder>(DIFF_CALLBACK) {
    override fun getItemCount(): Int {
        return goals.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancialGoalHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fin_goal_list_item, parent, false)

        return FinancialGoalHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: FinancialGoalHolder, position: Int) {
        val goal = goals[position]

        if(goal != null) {
            holder.bind(goal, clickListener)
        }

        else {
            holder.clear()
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FinancialGoal>() {
            override fun areItemsTheSame(oldItem: FinancialGoal, newItem: FinancialGoal) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: FinancialGoal, newItem: FinancialGoal) =
                oldItem == newItem
        }
    }

}