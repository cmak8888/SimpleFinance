package com.csci448.FP.simplefinance.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.math.BigDecimal
import java.math.RoundingMode

private const val logTag = "448.Dashboard"

class Dashboard : Fragment() {
    private lateinit var nameUser : TextView
    private lateinit var financialGoalName : TextView
    private lateinit var financialGoalDeadline : TextView
    private lateinit var financialGoalDesc : TextView
    private lateinit var financialGoalStartAmountGoal : TextView
    private lateinit var financialGoalProgressBar : ProgressBar
    private lateinit var financialGoalEndAmountGoal: TextView
    private lateinit var financialGoalLinearLayout: LinearLayout
//    private lateinit var financialGoalPage : ImageButton
//    private lateinit var backlogPage : ImageButton
//    private lateinit var graphPage : ImageButton
//    private lateinit var signOut : ImageButton

    private lateinit var dashboardViewModel : DashboardViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "Dashboard"
        Log.d(logTag, "OnCreate() called")
        val factory = DashboardViewModelFactory(requireContext())
        dashboardViewModel = ViewModelProvider(this, factory).get(
            DashboardViewModel::class.java)

//        nameUser.text = getString(R.string.hello) + dashboardViewModel.getUser().toString()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dashboard, container, false)

        nameUser = view.findViewById(R.id.user)
        financialGoalName = view.findViewById(R.id.fin_goal_name)
        financialGoalName.text = getString(R.string.fin_label)
        financialGoalDesc = view.findViewById(R.id.fin_goal_desc)
        financialGoalDeadline = view.findViewById(R.id.fin_goal_date)
        financialGoalStartAmountGoal = view.findViewById(R.id.starting_amount_goal)
        financialGoalEndAmountGoal = view.findViewById(R.id.ending_amount_goal)
        financialGoalProgressBar = view.findViewById(R.id.progressBar)
        financialGoalLinearLayout = view.findViewById(R.id.dash_to_fin_goal)



        Log.d(logTag,"onCreateView() called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.goalListLiveData.observe(viewLifecycleOwner,
            Observer{
                    goals -> goals?.let {
                Log.i(logTag, "Got goals ${goals.size}")
                val goal = dashboardViewModel.getRecentGoal(goals)
                if(goal.title == "") {
                    financialGoalName.text = "No Goal"
                    financialGoalStartAmountGoal.text = ""
                    financialGoalEndAmountGoal.text = ""
                    financialGoalProgressBar.visibility = View.INVISIBLE
                } else {
                    goal.run {
                        financialGoalName.text = title
                        financialGoalDesc.text = description
                        financialGoalDeadline.text = deadline
                        financialGoalStartAmountGoal.text = currentAmount.toString()
                        financialGoalEndAmountGoal.text = goalAmount.toString()
                        val currentAmountInTwoDecimals = BigDecimal(currentAmount).setScale(1, RoundingMode.HALF_EVEN)
                        currentAmount = currentAmountInTwoDecimals.toDouble()
                        financialGoalProgressBar.progress = ((currentAmount / goalAmount) * 100).toInt() or 0
//                        Log.d(logTag, "The progress is ${progressBar.progress}")
                        financialGoalProgressBar.visibility = View.VISIBLE
                    }
                    if(goal.id != null) {
                        financialGoalLinearLayout.setOnClickListener {
                            val action =
                                DashboardDirections.actionDashboardPageToFinancialGoalDetailFragment(
                                    goal.id
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.top_bar, menu)
    }
}