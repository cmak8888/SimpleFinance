package com.csci448.FP.simplefinance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.appcompat.widget.Toolbar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.csci448.FP.simplefinance.data.financialgoal_data.FinancialGoal
import com.csci448.FP.simplefinance.ui.finance_goal.FinancialGoalWorker
import com.csci448.FP.simplefinance.ui.finance_goal.detail.FinancialGoalDetailFragment
import com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment.EXTRA_GOAL_TITLE
import com.csci448.FP.simplefinance.ui.finance_goal.financialgoal_fragment.FinancialGoalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private const val LOG_TAG = "448.Dashboard"

class MainActivity : AppCompatActivity(){

    private lateinit var bottomNavigation : BottomNavigationView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var workManager: WorkManager


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
                workManager.enqueueUniquePeriodicWork(FinancialGoalFragment.GOAL_NAME, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: NavigationView = findViewById(R.id.nav_view)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        drawerLayout = findViewById(R.id.drawer_layout)
        workManager = WorkManager.getInstance(applicationContext)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboard_page, R.id.backlog_page, R.id.fin_goal_page, R.id.financialGoalForm, R.id.trans_page, R.id.transactionsFormFragment), drawerLayout)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
        makeNotification(FinancialGoalFragment.goalstoActivity)
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")

    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
        Log.d(LOG_TAG, "goals to activitu is ${FinancialGoalFragment.goalstoActivity}")

    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        fun createIntent(context: Context, goalTitle: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_GOAL_TITLE, goalTitle)
            }
        }
    }
}
