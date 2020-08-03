package com.csci448.FP.simplefinance.ui.transactions.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import java.util.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs



private const val ARG_TRANSACTION_ID = "transactionId"
private const val logTag = "detailtransfrag"
class TransactionsDetailFragment: Fragment() {
    private lateinit var transaction: Transactions
    private lateinit var transactionDetailViewModel: TransactionsDetailViewModel
    private lateinit var editButton : Button
    private lateinit var deleteButton : Button
    private lateinit var dateButton: Button
    private lateinit var detailTransaction : EditText
    private lateinit var amountTransactions: EditText
    private lateinit var spendingButton: Button
    private lateinit var depositButton: Button
    private var edit = false
    private val args: TransactionsDetailFragmentArgs by navArgs()
    override fun onAttach(context: Context){
        super.onAttach(context)
        Log.d(logTag, "onAttach called")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate called")
        transaction = Transactions()
        val factory = TransactionsDetailViewModelFactory(requireContext())
        transactionDetailViewModel = ViewModelProvider(this, factory)
            .get(TransactionsDetailViewModel::class.java)

        val transactionId: UUID = args.transactionId
        transactionDetailViewModel.loadTransaction(transactionId)
        requireActivity().title = transaction.title
        Log.d(logTag, "args bundle transaction ID: $args.transactionId")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.transactions_detail, container, false)
        editButton = view.findViewById(R.id.detail_trans_edit_button) as Button
        deleteButton = view.findViewById(R.id.detail_trans_delete_button) as Button
        detailTransaction = view.findViewById(R.id.detail_transaction) as EditText
        amountTransactions = view.findViewById(R.id.trans_current_amount) as EditText
        depositButton = view.findViewById(R.id.detail_trans_deposit) as Button
        spendingButton = view.findViewById(R.id.detail_trans_spending) as Button
        dateButton = view.findViewById(R.id.detail_trans_deadline)

        Log.d(logTag, "onCreateView called")
        updateUI()
        if(!edit){
            detailTransaction.isEnabled=false
            amountTransactions.isEnabled=false
        }
        editButton.setOnClickListener {
            if(!edit){
                edit = true
                amountTransactions.isEnabled = true
                detailTransaction.isEnabled = true
                editButton.text = "Save"
                depositButton.setOnClickListener {
                    transaction.spentOrEarned="earned"
                    depositButton.isEnabled = false
                    spendingButton.isEnabled = true
                    depositButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.aquamarine))
                    spendingButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.normal))
                }
                spendingButton.setOnClickListener {
                    transaction.spentOrEarned="spent"
                    spendingButton.isEnabled = false
                    depositButton.isEnabled = true
                    depositButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.normal))
                    spendingButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.aquamarine))
                }
            }
            else{
                edit = false
                editButton.text = "Edit"
                detailTransaction.isEnabled=false
                amountTransactions.isEnabled=false
                dateButton.text = Date().toString()
                transactionDetailViewModel.saveTransaction(transaction)
            }
        }
        deleteButton.setOnClickListener {
            transactionDetailViewModel.deleteTransaction(transaction)
            val action = TransactionsDetailFragmentDirections.actionTransactionsDetailFragmentToTransPage()
            findNavController().navigate(action)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionDetailViewModel.transactionsLiveData.observe(
            viewLifecycleOwner,
            Observer{ transactions ->
                transactions?.let{
                    this.transaction = transactions
                    updateUI()
                }
            })
        Log.d(logTag, "onViewCreated called")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated called")
    }


    private fun updateUI(){
        transaction.run{
            if(spentOrEarned=="spent"){
                spendingButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.aquamarine))
                depositButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.normal))
            }
            else if(spentOrEarned=="earned"){
                depositButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.aquamarine))
                spendingButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.normal))
            }
            amountTransactions.setText(amount.toString())
            detailTransaction.setText(title)
            dateButton.text = timestamp.toString()
        }
        Log.d(logTag, "ui updated")

    }
    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                transaction.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        val amtWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                transaction.amount = sequence.toString().toDouble()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        detailTransaction.addTextChangedListener(titleWatcher)
        amountTransactions.addTextChangedListener(amtWatcher)
    }

}