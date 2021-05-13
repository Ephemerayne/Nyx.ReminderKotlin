package com.ephemerayne.reminder.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.ephemerayne.reminder.model.Reminder
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class NotificationsImpl @Inject constructor(private val application: Application) : Notifications {

    private val alarmManager = application.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
    private val alarmIntent = Intent(application.applicationContext, AlarmReceiver::class.java)

    override fun sendNotification(reminder: Reminder) {
        setNotification(
            title = reminder.title,
            description = reminder.description,
            notificationId = reminder.id ?: 0,
            notificationTimeInMillis = reminder.dateTime
                .toInstant(OffsetDateTime.now().offset)
                .toEpochMilli()
        )
    }

    private fun getPendingIntent(id: Int) = PendingIntent.getBroadcast(
        application,
        id,
        alarmIntent,
        0,
    )

    override fun cancelNotification(id: Int) = alarmManager.cancel(getPendingIntent(id))

    private fun setNotification(
        title: String,
        description: String,
        notificationId: Int,
        notificationTimeInMillis: Long,
    ) {
        alarmIntent.putExtra(NotificationsService.NOTIFICATION_TITLE, title)
        alarmIntent.putExtra(NotificationsService.NOTIFICATION_DESCRIPTION, description)
        alarmIntent.putExtra(NotificationsService.NOTIFICATION_ID, notificationId)

        alarmManager.set(
            AlarmManager.RTC,
            notificationTimeInMillis,
            getPendingIntent(notificationId),
        )
    }
}