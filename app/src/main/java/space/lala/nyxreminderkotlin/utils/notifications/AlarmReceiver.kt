package space.lala.nyxreminderkotlin.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase

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
                CoroutineScope(Dispatchers.IO).launch {
                    RemindersDatabase.getDatabase(it)
                        .reminderDao()
                        .deleteReminder(intent.getIntExtra(NotificationsService.NOTIFICATION_ID, 0))
                }
            }
            return
        }

        context?.let { NotificationsService.enqueueWork(it, serviceIntent) }
    }
}