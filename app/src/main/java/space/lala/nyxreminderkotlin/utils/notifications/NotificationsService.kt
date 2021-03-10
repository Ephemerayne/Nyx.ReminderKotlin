package space.lala.nyxreminderkotlin.utils.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
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

    override fun onHandleWork(intent: Intent) {
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

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            // TODO: попробовать избавиться
            Notification.Builder(this)
        }.run {
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.delete_icon)
            setContentTitle(title)
            setContentText(desc)
            build()
        }

        notificationManager.notify(id, notification)
    }
}
