package com.csci448.FP.simplefinance.ui.backlog

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.csci448.FP.simplefinance.ui.dashboard.DashboardDirections
import com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment.FinancialGoalFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.backlog.*
import java.math.BigDecimal
import java.math.RoundingMode


private const val logTag = "448.Backlog"

class Backlog : Fragment(), View.OnClickListener {

    private var navController: NavController? = null

    private lateinit var nameUser : TextView
    private lateinit var financialGoalName : TextView
    private lateinit var financialGoalDeadline : TextView
    private lateinit var financialGoalDesc : TextView
    private lateinit var financialGoalStartAmountGoal : TextView
    private lateinit var financialGoalProgressBar : ProgressBar
    private lateinit var financialGoalEndAmountGoal: TextView
    private lateinit var financialGoalPage : LinearLayout

    //Transaction 1
    private lateinit var trans1 : ConstraintLayout
    private lateinit var transtitle1 : TextView
    private lateinit var transdate1 : TextView
    private lateinit var transamount1 : TextView

    //Transaction 2
    private lateinit var trans2 : ConstraintLayout
    private lateinit var transtitle2 : TextView
    private lateinit var transdate2 : TextView
    private lateinit var transamount2 : TextView

    //Transaction 3
    private lateinit var trans3 : ConstraintLayout
    private lateinit var transtitle3 : TextView
    private lateinit var transdate3 : TextView
    private lateinit var transamount3 : TextView

    private lateinit var viewalltrans : Button

    private lateinit var backlogViewModel: BacklogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "My Money"
        val factory = BacklogViewModelFactory(requireContext())
        backlogViewModel = ViewModelProvider(this, factory).get(
            BacklogViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.backlog, container, false)


        nameUser = view.findViewById(R.id.user)
        financialGoalName = view.findViewById(R.id.fin_goal_name)
        financialGoalDesc = view.findViewById(R.id.fin_goal_desc)
        financialGoalDeadline = view.findViewById(R.id.fin_goal_date)
        financialGoalStartAmountGoal = view.findViewById(R.id.starting_amount_goal)
        financialGoalEndAmountGoal = view.findViewById(R.id.ending_amount_goal)
        financialGoalProgressBar = view.findViewById(R.id.progressBar)
        financialGoalPage = view.findViewById(R.id.backlog_to_fin_goal)

        trans1 = view.findViewById(R.id.trans_list_1)
        trans2 = view.findViewById(R.id.trans_list_2)
        trans3 = view.findViewById(R.id.trans_list_3)
        transtitle1 = view.findViewById(R.id.trans_title_1)
        transtitle2 = view.findViewById(R.id.trans_title_2)
        transtitle3 = view.findViewById(R.id.trans_title_3)
        transdate1 = view.findViewById(R.id.trans_date_1)
        transdate2 = view.findViewById(R.id.trans_date_2)
        transdate3 = view.findViewById(R.id.trans_date_3)
        transamount1 = view.findViewById(R.id.trans_amount_1)
        transamount2 = view.findViewById(R.id.trans_amount_2)
        transamount3 = view.findViewById(R.id.trans_amount_3)

        viewalltrans = view.findViewById(R.id.view_all_trans)

        nameUser.text = "Welcome!"


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated Called")

        navController = Navigation.findNavController(view)
        backlogViewModel.goalListLiveData.observe(viewLifecycleOwner,
            Observer{
                    goals -> goals?.let {
//                Log.i(com.csci448.FP.simplefinance.ui.dashboard.logTag, "Got goals ${goals.size}")
                val goal = backlogViewModel.getRecentGoal(goals)
                if(goal.id == null) {
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
                        val currentAmountInTwoDecimals = BigDecimal(goal.currentAmount).setScale(1, RoundingMode.HALF_EVEN)
                        goal.currentAmount = currentAmountInTwoDecimals.toDouble()
                        financialGoalProgressBar.progress = ((goal.currentAmount / goal.goalAmount) * 100).toInt() or 0
//                        Log.d(logTag, "The progress is ${progressBar.progress}")
                        financialGoalProgressBar.visibility = View.VISIBLE
                    }
                    if(goal.title != "") {
                        financialGoalPage.setOnClickListener {
                            val action =
                                BacklogDirections.actionBacklogPageToFinancialGoalDetailFragment(
                                    goal.id
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
            })
        backlogViewModel.transactionListLiveData.observe(viewLifecycleOwner,
            Observer { transactions ->
                transactions?.let {
                    val transactionList: List<Transactions> =
                        backlogViewModel.getMostRecentTransactions(transactions)
                    if (transactionList[0].title != "") {
                        transactionList[0].run {
                            transtitle1.text = title
                            transdate1.text = timestamp.toString()
                            transamount1.text = amount.toString()
                        }
                        trans1.setOnClickListener {
                            val action =
                                BacklogDirections.actionBacklogPageToFinancialGoalDetailFragment(
                                    transactionList[0].id
                                )
                            findNavController().navigate(action)
                        }
                        if (transactionList[1].title != "") {
                            transactionList[1].run {
                                transtitle2.text = title
                                transdate2.text = timestamp.toString()
                                transamount2.text = amount.toString()
                            }
                            trans2.setOnClickListener {
                                val action =
                                    BacklogDirections.actionBacklogPageToFinancialGoalDetailFragment(
                                        transactionList[1].id
                                    )
                                findNavController().navigate(action)
                            }
                            if (transactionList[2].title != "") {
                                transactionList[2].run {
                                    transtitle3.text = title
                                    transdate3.text = timestamp.toString()
                                    transamount3.text = amount.toString()
                                }
                                trans3.setOnClickListener {
                                    val action =
                                        BacklogDirections.actionBacklogPageToFinancialGoalDetailFragment(
                                            transactionList[2].id
                                        )
                                    findNavController().navigate(action)
                                }
                            }
                        }
                    }
                }
            })
        viewalltrans.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.top_bar, menu)
    }


    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.view_all_trans -> navController!!.navigate(R.id.action_backlog_page_to_trans_page)
        }
    }
}