package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_form

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class FinancialGoalFormFragment: Fragment() {
    private val logTag = "448.GoalFormFrag"
    private lateinit var fg: FinancialGoal
    private lateinit var fgFormViewModel: FinancialGoalFormFragmentViewModel
    private lateinit var financialGoalTitle: EditText
    private lateinit var financialGoalDescription: EditText
    private lateinit var financialGoalAmount: EditText
    //private lateinit var financialGoalDeadline: EditText
    private lateinit var dailyButton: Button
    private lateinit var weeklyButton: Button
    private lateinit var monthlyButton: Button
    private lateinit var annuallyButton: Button
    private lateinit var addGoalButton: Button
    private lateinit var datePicker: DatePicker
    private var isDailyClicked = false
    private var isWeeklyClicked = false
    private var isMonthlyClicked = false
    private var isAnnuallyClicked = false
    private var financialGoal: FinancialGoal = FinancialGoal()
    private val frequencySelected = Color.rgb(127, 255, 212)
    private var isValidDateFormat = false
    private var isValidAmount = false
    private var isConfirmedSave = false
    private var isValidMonth = false
    private var isValidDay = false
    private var isValidDate = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        fg = FinancialGoal()

        requireActivity().title = "Creating a Financial Goal"
        val factory = FinancialGoalFormFragmentViewModelFactory(requireContext())
        fgFormViewModel = ViewModelProvider(this, factory).get(FinancialGoalFormFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view: View = inflater.inflate(R.layout.financialgoal_form, container, false)

        financialGoalTitle = view.findViewById(R.id.fin_goal_title_form)
        financialGoalAmount = view.findViewById(R.id.fin_goal_amount_form)
        financialGoalDescription = view.findViewById(R.id.fin_goal_desc_form)
        //financialGoalDeadline = view.findViewById(R.id.fin_goal_deadline_form)
        dailyButton = view.findViewById(R.id.daily_button)
        weeklyButton = view.findViewById(R.id.weekly_button)
        monthlyButton = view.findViewById(R.id.monthly_button)
        annuallyButton = view.findViewById(R.id.annually_button)
        addGoalButton = view.findViewById(R.id.add_fin_goal)
        datePicker = view.findViewById(R.id.fin_goal_deadline_form)
        dailyButton.setOnClickListener {
            isDailyClicked = true
            isWeeklyClicked = false
            isMonthlyClicked = false
            isAnnuallyClicked = false

            dailyButton.setBackgroundColor(frequencySelected)
            weeklyButton.setBackgroundColor(Color.WHITE)
            monthlyButton.setBackgroundColor(Color.WHITE)
            annuallyButton.setBackgroundColor(Color.WHITE)
        }

        weeklyButton.setOnClickListener {
            isDailyClicked = false
            isWeeklyClicked = true
            isMonthlyClicked = false
            isAnnuallyClicked = false

            dailyButton.setBackgroundColor(Color.WHITE)
            monthlyButton.setBackgroundColor(Color.WHITE)
            annuallyButton.setBackgroundColor(Color.WHITE)
            weeklyButton.setBackgroundColor(frequencySelected)
        }

        monthlyButton.setOnClickListener {
            isDailyClicked = false
            isWeeklyClicked = false
            isMonthlyClicked = true
            isAnnuallyClicked = false

            dailyButton.setBackgroundColor(Color.WHITE)
            weeklyButton.setBackgroundColor(Color.WHITE)
            monthlyButton.setBackgroundColor(frequencySelected)
            annuallyButton.setBackgroundColor(Color.WHITE)

        }

        annuallyButton.setOnClickListener {
            isDailyClicked = false
            isWeeklyClicked = false
            isMonthlyClicked = true
            isAnnuallyClicked = false


            annuallyButton.setBackgroundColor(frequencySelected)
            dailyButton.setBackgroundColor(Color.WHITE)
            weeklyButton.setBackgroundColor(Color.WHITE)
            monthlyButton.setBackgroundColor(Color.WHITE)
        }

        val date = Calendar.getInstance()
        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayYear = today.get(Calendar.YEAR)
        var dateSelected = ""

        datePicker.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)

        ) { _, year, theMonth, day ->
            val month = theMonth + 1
            date.set(year, month, day)

            dateSelected = "$month/$day/$year"

            if(year > todayYear) {

                isValidDate = true
                financialGoal.deadline = dateSelected
            }

            else if(year == todayYear) {
                if(month > todayMonth) {
                    isValidDate = true
                    financialGoal.deadline = dateSelected
                }

                else if(month == todayMonth) {
                    System.out.println("dasfsadf")
                    if(day > todayDay) {
                        System.out.println("dfdasf")
                        isValidDate = true
                        financialGoal.deadline = dateSelected
                    }

                    else {
                        System.out.println("dfds")
                        isValidDate = false
                    }
                }

                else {
                    isValidDate = false
                }

            }

            else if(year == todayYear && month == todayMonth && day == todayDay) {
                isValidDate = true
                financialGoal.deadline = dateSelected
            }

            else {
                isValidDate = false
            }


        }

        addGoalButton.setOnClickListener{
            submitForm()



            if(isValidAmount && isValidDate) {
                isConfirmedSave = true
                fgFormViewModel.addGoal(financialGoal)
                val action = FinancialGoalFormFragmentDirections.actionFinancialGoalFormToFinGoalPage()
                findNavController().navigate(action)
            }

            else {
                Toast.makeText(this.context, "Your inputs are invalid", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
    }

    private fun submitForm() {
        val title = financialGoalTitle.text.toString()
        val amount =  financialGoalAmount.text.toString()
        val description = financialGoalDescription.text.toString()
        var frequency = ""


        isValidAmount = amount.toDoubleOrNull() != null


        if(isDailyClicked) {
            frequency = "daily"
        }

        else if(isWeeklyClicked) {
            frequency = "weekly"
        }

        else if(isMonthlyClicked) {
            frequency = "monthly"
        }

        else if(isAnnuallyClicked) {
            frequency = "annually"
        }


        if(isValidAmount) {
            if(amount.toDouble() > 0) {
                financialGoal.goalAmount = amount.toDouble()
            }
        }



        financialGoal.title = title
        financialGoal.description = description
        financialGoal.frequency = frequency


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