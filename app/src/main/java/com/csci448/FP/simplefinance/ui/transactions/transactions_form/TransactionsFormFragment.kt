package com.csci448.FP.simplefinance.ui.transactions.transactions_form

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.csci448.FP.simplefinance.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import kotlinx.android.synthetic.main.transaction_form.*


private const val ARG_TRANSACTION_ID ="transaction_id"
class TransactionsFormFragment: Fragment()  {

    private var navController: NavController? = null
    private lateinit var transaction: Transactions
    private lateinit var titleField: EditText
    private lateinit var transactionsFormViewModel: TransactionsFormViewModel
    private lateinit var dateField: EditText
    private lateinit var transactionAmount: EditText
    private lateinit var depositButton: Button
    private lateinit var spendingButton: Button
    private lateinit var addTransactionButton: Button
    private var isSpendingClicked = false
    private var isDepositClicked = false
    private val logTag = "448.TransFormFrag"
    private val frequencySelected = Color.rgb(127, 255, 212)
    private var isValidAmount = false
    private var isConfirmedSave = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "Create a New Transaction"
        transaction= Transactions()
        val factory = TransactionsFormViewModelFactory(requireContext())
        transactionsFormViewModel = ViewModelProvider(this, factory).get(TransactionsFormViewModel::class.java)
        Log.d(logTag, "onCreate() called")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag, "onCreateView() called")
        val view: View = inflater.inflate(R.layout.transaction_form, container, false)
        titleField = view.findViewById(R.id.trans_text_form)as EditText
        transactionAmount = view.findViewById(R.id.trans_amount_form)as EditText
        spendingButton = view.findViewById(R.id.trans_spending_form)as Button
        depositButton = view.findViewById(R.id.trans_deposit_form)as Button
        addTransactionButton = view.findViewById(R.id.trans_add_form)as Button
        spendingButton.setOnClickListener{
            isDepositClicked=false
            isSpendingClicked=true
            spendingButton.setBackgroundColor(frequencySelected)
            depositButton.setBackgroundColor(Color.WHITE)
        }
        depositButton.setOnClickListener {
            isDepositClicked=true
            isSpendingClicked=false
            depositButton.setBackgroundColor(frequencySelected)
            spendingButton.setBackgroundColor(Color.WHITE)
        }
        addTransactionButton.setOnClickListener {
            submitForm()
            if(isValidAmount){
                isConfirmedSave = true
                transactionsFormViewModel.addTransaction(transaction)
                val action = TransactionsFormFragmentDirections.actionTransactionsFormFragmentToTransPage()
                navController!!.navigate(action)
            }
            else{
                Toast.makeText(this.context, "Your inputs are invalid", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        transactionsFormViewModel.transactionsLiveData.observe(
            viewLifecycleOwner,
            Observer{ transaction ->
                transaction?.let{
                    this.transaction = transaction
                    updateUI()
                }
            }
        )
        Log.d(logTag, "onViewCreated() called")
    }

    private fun updateUI() {
        trans_text_form.setText(transaction.title)
    }
    private fun submitForm(){
        val title = titleField.text.toString()
        val amount =  transactionAmount.text.toString()
        var type = ""
        isValidAmount = amount.toDoubleOrNull() != null

        if(isSpendingClicked) {
            type = "spent"
        }

        else if(isDepositClicked) {
            type = "earned"
        }

        if(isValidAmount) {
            if(amount.toDouble() > 0) {
                transaction.amount = amount.toDouble()
            }
        }

        transaction.title = title
        transaction.spentOrEarned = type
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()

    }

    override fun onDestroyView() {
        Log.d(logTag, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach() called")
        super.onDetach()
    }
}