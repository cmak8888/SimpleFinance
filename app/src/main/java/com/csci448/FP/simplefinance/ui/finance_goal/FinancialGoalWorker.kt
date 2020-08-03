package com.csci448.FP.simplefinance.ui.finance_goal

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.renderscript.RenderScript
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.csci448.FP.simplefinance.MainActivity
import com.csci448.FP.simplefinance.R
import com.google.common.util.concurrent.ListenableFuture
import java.security.Provider
import java.util.concurrent.Executor
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit

class FinancialGoalWorker(private val context: Context, workerParams: WorkerParameters): ListenableWorker(context, workerParams) {

    private val logTag = "448.locatrWork"

    override fun startWork(): ListenableFuture<Result> {
        Log.d(logTag, "Work triggered")

        return CallbackToFutureAdapter.getFuture { completer ->


            Log.d(logTag, "Got a title: ")
            val notificationManager = NotificationManagerCompat.from(context)
            val channelID = context.resources.getString(R.string.notification_channel_id)
//

            val intent = MainActivity.createIntent(context, "goal").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelID,
                    "Simple Finance",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = R.string.notification_channel_desc.toString()
                }
                notificationManager.createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Goal Has Expired")
                    .setContentText("One of your financial goals has expired.")
                    .setContentIntent(pendingIntent).setAutoCancel(true).build()

                notificationManager.notify(0, notification)
            }
            completer.set(Result.success())

//            fusedLocationProviderClient.lastLocation.addOnFailureListener { location ->
//                Log.d(logTag, "Got a location in failure: $location")
//                completer.set(Result.failure())
//
//            }
//
//            fusedLocationProviderClient.lastLocation.addOnCanceledListener {
//                completer.set(Result.failure())
//
//            }


        }
    }




}