package com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.work.*
import com.csci448.FP.simplefinance.MainActivity
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.ui.finance_goal.FinancialGoalWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.TimeUnit


const val EXTRA_GOAL_TITLE = "extra title"

class FinancialGoalFragment: Fragment() {

    private val logTag = "448.FGScreenFrag"

    //private lateinit var navController: NavController
    private lateinit var fgFragmentViewModel: FinancialGoalFragmentViewModel
    private lateinit var fgRecyclerView: RecyclerView
    private lateinit var adapter: FinancialGoalAdapter
    private lateinit var fgSearchView: SearchView
    private lateinit var fab: FloatingActionButton
    private lateinit var workManager: WorkManager
    private lateinit var tempGoals: List<FinancialGoal>

    private var mediaPlayer : MediaPlayer? = null



    private fun updateUI(goals: List<FinancialGoal>, query: String) {

        val filteredGoals = fgFragmentViewModel.applyFilter(goals, query)
        adapter = FinancialGoalAdapter(filteredGoals) { goal: FinancialGoal -> Unit
            val action = FinancialGoalFragmentDirections.actionFinGoalPageToFinancialGoalDetailFragment(goal.id)
            findNavController().navigate(action)
        }
        fgRecyclerView.adapter = adapter
    }



    private fun updateDeadlines(goals: List<FinancialGoal>) {

        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayYear = today.get(Calendar.YEAR)
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val todayString = "$todayMonth/$todayDay/$todayYear"
        val todayDate = sdf.parse(todayString)

        for(i in goals.indices) {
            val deadlineDate = sdf.parse(goals[i].deadline)
            System.out.println("Deadline: ${goals[i].deadline}")
            System.out.println("Today: $today")
            System.out.println("Today as date: $todayDate")
            System.out.println("Deadline as date $deadlineDate")

            if(todayDate.after(deadlineDate)) {

                println("Inside if statement")
                if (goals[i].frequency == "daily") {
                    today.add(Calendar.DAY_OF_MONTH, 1)
                    goals[i].deadline = "${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.YEAR)}"
                }

                else if(goals[i].frequency == "weekly") {
                    today.add(Calendar.DAY_OF_MONTH, 7)
                    goals[i].deadline = "${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.YEAR)}"
                }

                else if(goals[i].frequency == "monthly") {
                    today.add(Calendar.MONTH, 1)
                    goals[i].deadline = "${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.YEAR)}"
                }

                else if(goals[i].frequency == "annually") {
                    today.add(Calendar.YEAR, 1)
                    goals[i].deadline = "${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.YEAR)}"
                }


//                val workRequest = OneTimeWorkRequestBuilder<FinancialGoalWorker>().build()
//                workManager.enqueueUniqueWork(GOAL_NAME, ExistingWorkPolicy.APPEND, workRequest)

                fgFragmentViewModel.updateGoal(goals[i])

            }
        }
    }

    fun makeNotification(goals: List<FinancialGoal>) {

        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayYear = today.get(Calendar.YEAR)
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val todayString = "$todayMonth/$todayDay/$todayYear"
        val todayDate = sdf.parse(todayString)

        for(i in goals.indices) {
            val deadlineDate = sdf.parse(goals[i].deadline)
            System.out.println("Deadline: ${goals[i].deadline}")
            System.out.println("Today: $today")
            System.out.println("Today as date: $todayDate")
            System.out.println("Deadline as date $deadlineDate")

            if (todayDate.after(deadlineDate)) {
                val periodicWorkRequest = PeriodicWorkRequestBuilder<FinancialGoalWorker>(1, TimeUnit.MINUTES).build()
                workManager.enqueueUniquePeriodicWork(GOAL_NAME, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
            }
        }
     }


    fun clearList() {
        updateUI(emptyList(), "")
    }

    fun restartList(query: String?) {
        val newQuery = query ?: ""
        fgFragmentViewModel.goalListLiveData.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let {
                Log.i(logTag, "Got Goals ${goals.size}")
                updateUI(goals, newQuery)
            }

        })
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        requireActivity().title = "My Financial Goals"
        val factory = FinancialGoalFragmentViewModelFactory(requireContext())
        fgFragmentViewModel = ViewModelProvider(this, factory).get(FinancialGoalFragmentViewModel::class.java)
//        workManager = WorkManager.getInstance(requireContext())
        //NavigationUI.setupWithNavController(toolbar, NavHostFragment.findNavController(nav_host_fragment))



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.financial_goal_list, container, false)

        //navController = Navigation.findNavController(view)
        fgRecyclerView = view.findViewById(R.id.financial_goal_list_recycler_view) as RecyclerView
        fgRecyclerView.layoutManager = LinearLayoutManager(context)
        fgSearchView = view.findViewById(R.id.fg_search_view)

        clearList()

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { _: View ->
            val action =
                FinancialGoalFragmentDirections.actionFinGoalPageToFinancialGoalFormFragment()
            findNavController().navigate(action)
            mediaPlayer = MediaPlayer.create(requireContext(),R.raw.click)
            mediaPlayer?.setOnPreparedListener{
                Log.d(logTag, "player prepared")
            }
            mediaPlayer?.start()
        }
        
        fgSearchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                clearList()
                restartList(fgSearchView.query.toString())
            }
        }




        Log.d(logTag,"onCreateView() called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        restartList("")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag,"onActivityCreated() called")
    }


    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val queryListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                clearList()
                restartList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        }


        fgSearchView.setOnQueryTextListener(queryListener)
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")

        fgFragmentViewModel.goalListLiveData.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let {
                Log.i(logTag, "Got Goals ${goals.size}")
                updateDeadlines(goals)
                goalstoActivity = goals
                tempGoals = goals
            }
        })

    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
        makeNotification(tempGoals)
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


    companion object {
        const val GOAL_NAME = "goal name"
        var goalstoActivity: List<FinancialGoal> = emptyList()
    }
}