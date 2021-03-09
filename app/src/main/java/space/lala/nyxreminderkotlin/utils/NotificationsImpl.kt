package space.lala.nyxreminderkotlin.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.ui.dialogSheet.ViewReminderDialogSheet
import javax.inject.Inject

class NotificationsImpl @Inject constructor(private val application: Application) : Notifications {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
    }

    override fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChannelName"
            val description = "ChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }
            val notificationManager: NotificationManager = application.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun sendNotification(reminder: Reminder) {
        val intent = Intent(application, ViewReminderDialogSheet::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(application, 0, intent, 0)

        val builder = NotificationCompat.Builder(application, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(reminder.title)
//            .setContentText(reminder.description)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    reminder.description
                )
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(application)) {
            notify(reminder.id ?: 0, builder.build())
        }
    }
}