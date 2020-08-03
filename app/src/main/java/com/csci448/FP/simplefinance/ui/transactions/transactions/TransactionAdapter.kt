package com.csci448.FP.simplefinance.ui.transactions.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.transactions_data.Transactions


class TransactionAdapter(private val transactions: List<Transactions>, private val clickListener: (Transactions) -> Unit)
    : RecyclerView.Adapter<TransactionHolder>() {

    override fun getItemCount(): Int {
        return transactions.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trans_list_item, parent, false)
        return TransactionHolder(view)
    }
    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transactions = transactions[position]
        if(transactions!=null){
            holder.bind(transactions, clickListener)
        }
        else{
            holder.clear()
        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transactions>() {
            override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions) =
                oldItem == newItem
        }
    }
}