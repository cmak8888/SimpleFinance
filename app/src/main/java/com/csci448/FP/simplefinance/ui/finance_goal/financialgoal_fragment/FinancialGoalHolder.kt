package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal

class FinancialGoalHolder(val view: View): RecyclerView.ViewHolder(view) {
    private lateinit var fg: FinancialGoal
    private val title: TextView = itemView.findViewById(R.id.fin_goal_title)
    private val deadline: TextView = itemView.findViewById(R.id.fin_deadline_date)
    //TODO: Work on Progress Bar

    fun bind(fg: FinancialGoal, clickListener: (FinancialGoal) -> Unit) {
        this.fg = fg
        itemView.setOnClickListener{ clickListener(this.fg)}
        title.text = this.fg.title
        deadline.text = this.fg.deadline

    }


    fun clear() {

    }

}