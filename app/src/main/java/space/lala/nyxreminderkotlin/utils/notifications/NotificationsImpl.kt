package space.lala.nyxreminderkotlin.utils.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.threeten.bp.ZoneOffset
import space.lala.nyxreminderkotlin.model.Reminder
import javax.inject.Inject

class NotificationsImpl @Inject constructor(private val application: Application) : Notifications {
    override fun sendNotification(reminder: Reminder) {
        setNotification(
            title = reminder.title,
            description = reminder.description,
            notificationId = reminder.id ?: 0,
            notificationTimeInMillis = reminder.dateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
            context = application,
        )
    }

    private fun setNotification(
        title: String,
        description: String,
        notificationId: Int,
        notificationTimeInMillis: Long,
        context: Context
    ) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context.applicationContext, AlarmReceiver::class.java)

        alarmIntent.putExtra(NotificationsService.NOTIFICATION_TITLE, title)
        alarmIntent.putExtra(NotificationsService.NOTIFICATION_DESCRIPTION, description)
        alarmIntent.putExtra(NotificationsService.NOTIFICATION_ID, notificationId)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT,
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            notificationTimeInMillis,
            pendingIntent,
        )
    }
}