package space.lala.nyxreminderkotlin.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.R

class NotificationsService : JobIntentService() {

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"

        const val NOTIFICATION_TITLE = "NOTIFICATION_TITLE"
        const val NOTIFICATION_DESCRIPTION = "NOTIFICATION_DESCRIPTION"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"

        private const val JOB_ID = 1

        fun enqueueWork(context: Context, notificationIntent: Intent) {
            enqueueWork(
                context,
                NotificationsService::class.java,
                JOB_ID,
                notificationIntent,
            )
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChannelName"
            val description = "ChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    this.description = description
                    enableVibration(true)
                    enableLights(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }

            val notificationManager: NotificationManager = application.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onHandleWork(intent: Intent) {
        createChannel()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(this, MainActivity::class.java)

        val title = intent.getStringExtra(NOTIFICATION_TITLE)
        val desc = intent.getStringExtra(NOTIFICATION_DESCRIPTION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val deleteIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_DELETE
            putExtra(NOTIFICATION_ID, 0)
        }

        val deletePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            deleteIntent,
            0
        )

        val closeIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_CLOSE
        }

        val closePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            closeIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.delete_icon)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(
                    R.drawable.delete_icon,
                    getString(R.string.delete_reminder),
                    deletePendingIntent
                )
                .addAction(
                    R.drawable.icon_dialog_close,
                    getString(R.string.cancel_button_dialog),
                    closePendingIntent
                )
                .build()

        notificationManager.notify(id, builder)
    }
}
