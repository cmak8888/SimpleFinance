package com.csci448.FP.simplefinance.ui.transactions.transactions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import org.w3c.dom.Text

class TransactionHolder(val view: View) : RecyclerView.ViewHolder(view){
    private lateinit var transactions: Transactions
    private val titleTextView: TextView = itemView.findViewById(R.id.trans_title)
    private val dateTextView: TextView = itemView.findViewById(R.id.trans_date)
    private val amountTextView: TextView = itemView.findViewById(R.id.trans_amount)
    fun bind(transactions: Transactions, clickListener: (Transactions) -> Unit ) {
        this.transactions = transactions
        itemView.setOnClickListener { clickListener(this.transactions) }
        titleTextView.text = this.transactions.title
        dateTextView.text = this.transactions.timestamp.toString()
        amountTextView.text = this.transactions.amount.toString().format("%.2f")
    }
    fun clear(){

    }
}