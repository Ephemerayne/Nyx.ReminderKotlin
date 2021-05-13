package com.ephemerayne.reminder.notifications

import android.app.Activity
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ephemerayne.reminder.datasource.local.database.RemindersDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_DELETE = "ACTION_DELETE"
        const val ACTION_CLOSE = "ACTION_CLOSE"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        val serviceIntent = Intent(context, NotificationsService::class.java)

        intent?.run {
            serviceIntent.putExtra(
                NotificationsService.NOTIFICATION_TITLE,
                getStringExtra(NotificationsService.NOTIFICATION_TITLE),
            )
            serviceIntent.putExtra(
                NotificationsService.NOTIFICATION_DESCRIPTION,
                getStringExtra(NotificationsService.NOTIFICATION_DESCRIPTION),
            )
            serviceIntent.putExtra(
                NotificationsService.NOTIFICATION_ID,
                getIntExtra(NotificationsService.NOTIFICATION_ID, 0),
            )
        }

        val receivedAction = intent?.action

        if (receivedAction == ACTION_DELETE) {
            context?.let {
                deleteReminderFromDatabase(it, intent)
                cancelNotification(it, intent)
            }
            return
        }

        context?.let { NotificationsService.enqueueWork(it, serviceIntent) }
    }

    private fun deleteReminderFromDatabase(context: Context, intent: Intent) {
        context.let {
            CoroutineScope(Dispatchers.IO).launch {
                RemindersDatabase.getDatabase(it)
                    .reminderDao()
                    .deleteReminder(intent.getIntExtra(NotificationsService.NOTIFICATION_ID, 0))
            }
        }
    }

    private fun cancelNotification(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(intent.getIntExtra(NotificationsService.NOTIFICATION_ID, 0))
    }
}