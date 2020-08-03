package com.csci448.FP.simplefinance.ui.finance_goal.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.ui.finance_goal.FinancialGoalWorker
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.TimeUnit

private const val logTag = "448.FinanceDetail"
private const val ARG_GOAL_ID = "goal_id"

class FinancialGoalDetailFragment : Fragment() {
    private lateinit var goal: FinancialGoal

    private var edit: Boolean = false
    private var addMoneyEdit: Boolean = false
    private lateinit var titleField: EditText
    private lateinit var description: EditText
    private lateinit var date: Button
    private lateinit var currentAmount: EditText

//    private lateinit var endAmount: EditText
    private lateinit var repeatSpinner: Spinner
//    private lateinit var addMoney: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var financialDetailViewModel: FinancialGoalDetailFragmentViewModel
    private lateinit var progressBar: ProgressBar
    private val args: FinancialGoalDetailFragmentArgs by navArgs()


    private fun updateUI() {
        titleField.setText(goal.title)
        description.setText(goal.description)
        date.text = goal.deadline
//        currentAmount.setText(goal.currentAmount.toString())
//        endAmount.setText(goal.goalAmount.toString())
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        goal = FinancialGoal( )


        val factory = FinancialGoalDetailFragmentViewModelFactory(requireContext())
        financialDetailViewModel = ViewModelProvider(this, factory).get(FinancialGoalDetailFragmentViewModel::class.java)

        val goalId: UUID = args.goalId
        financialDetailViewModel.loadGoal(goalId)
        requireActivity().title = goal.title
        Log.d(logTag, "args bundle goal ID: $goalId")
        //Eventually, load goal from database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag,"onCreateView() called")

        val view : View = inflater.inflate(R.layout.financialgoal_detail, container, false)

        titleField = view.findViewById(R.id.detail_fin_goal)
        description = view.findViewById(R.id.detail_fin_desc)
        date = view.findViewById(R.id.detail_fin_deadline)
        currentAmount = view.findViewById(R.id.current_amount)
//        endAmount = view.findViewById(R.id.detail_fin_end)
        repeatSpinner = view.findViewById(R.id.repeat)
        progressBar = view.findViewById(R.id.detail_fin_progress_bar)
        var currentAmountAsDouble = 0.0
//        addMoney = view.findViewById(R.id.detail_fin_add_money)
        editButton = view.findViewById(R.id.detail_fin_edit_button)
        deleteButton = view.findViewById(R.id.detail_fin_delete_button)
////        //Creates the Spinner Adapter
//        val adapter = ArrayAdapter.createFromResource(this.context,
//            R.array.repeat_list, android.R.layout.simple_spinner_item)
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Apply the adapter to the spinner
//        spinner.adapter = adapter
        updateUI()
        if(!edit) {
            titleField.isEnabled = false
            description.isEnabled = false
            date.isEnabled = false
//            currentAmount.isEnabled = false
//            endAmount.isEnabled = false
        }

        if(!addMoneyEdit) {
            currentAmount.isEnabled = false
        }

        System.out.println("The current amount is ${goal.currentAmount}")
        //progressBar.progress = 0 //((goal.currentAmount / goal.goalAmount) * 100).toInt()


//        addMoney.setOnClickListener{
//
//            if(!addMoneyEdit) {
//                addMoneyEdit = true
//                currentAmount.isEnabled = true
//            }
//
//            else {
//                addMoneyEdit = false
//                currentAmount.isEnabled = false
//
//                if(currentAmount.text.toString().isEmpty()) {
//                    currentAmountAsDouble = 0.0
//                }
//                else {
//                    currentAmountAsDouble = currentAmount.text.toString().toDouble()
//                }
//                goal.currentAmount = currentAmountAsDouble
//                currentAmount.setText(goal.currentAmount.toString())
//                saveGoal()
//            }
//        }



        editButton.setOnClickListener{
            if(!edit) {
                edit = true
                editButton.text = getString(R.string.update)
                titleField.isEnabled = true
                description.isEnabled = true
                date.isEnabled = true
                currentAmount.isEnabled = true
//                endAmount.isEnabled = true
            } else {
                edit = false
                editButton.text = getString(R.string.edit)
                titleField.isEnabled = false
                description.isEnabled = false
                date.isEnabled = false
                currentAmount.isEnabled = false
                currentAmountAsDouble = currentAmount.text.toString().toDouble()
                goal.currentAmount = currentAmountAsDouble
                currentAmount.setText(goal.currentAmount.toString())

//                endAmount.isEnabled = false
                saveGoal()
            }
        }


        deleteButton.setOnClickListener{
            financialDetailViewModel.deleteGoal(goal)
            val action = FinancialGoalDetailFragmentDirections.actionFinancialGoalDetailFragmentToFinGoalPage()
            findNavController().navigate(action)
        }


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        financialDetailViewModel.goalLiveData.observe(
            viewLifecycleOwner,
            Observer {goal -> goal?.let {
                this.goal = goal
                progressBar.progress = ((this.goal.currentAmount / this.goal.goalAmount) * 100).toInt()
                var editable = Editable.Factory.getInstance().newEditable(this.goal.currentAmount.toString().format("%.2f"))
                currentAmount.text = editable
                updateUI()
            }})
        Log.d(logTag, "onViewCreated() called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag,"onActivityCreated() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged( sequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                goal.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        val descWatcher = object : TextWatcher {
            override fun beforeTextChanged( sequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                goal.description = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        val dateWatcher = object : TextWatcher {
            override fun beforeTextChanged( sequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                goal.deadline = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }

        val currentAmountWatcher = object : TextWatcher {
            override fun beforeTextChanged( sequence: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {

                if(sequence.isNullOrEmpty()) {
                    goal.currentAmount = 0.0
                    progressBar.progress = 0
                }

                else {
                    val currentAmountInTwoDecimals = BigDecimal(sequence.toString().toDouble()).setScale(1, RoundingMode.HALF_EVEN)
                    goal.currentAmount = currentAmountInTwoDecimals.toDouble()
                    progressBar.progress = ((goal.currentAmount / goal.goalAmount) * 100).toInt()
                    Log.d(logTag, "The progress is ${progressBar.progress}")
                }
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)
        description.addTextChangedListener(descWatcher)
        date.addTextChangedListener(dateWatcher)
        currentAmount.addTextChangedListener(currentAmountWatcher)


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

    private fun saveGoal() {
        financialDetailViewModel.updateGoal(goal)
    }

    private fun getPosition() {

    }

    companion object {
        fun newInstance(goalId: UUID): FinancialGoalDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GOAL_ID, goalId)
            }
            return FinancialGoalDetailFragment().apply {
                arguments = args
            }
        }
    }
}