package space.lala.nyxreminderkotlin.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = Intent(context, NotificationsService::class.java)

        intent?.run {
            service.putExtra(
                NotificationsService.NOTIFICATION_TITLE,
                getStringExtra(NotificationsService.NOTIFICATION_TITLE),
            )
            service.putExtra(
                NotificationsService.NOTIFICATION_DESCRIPTION,
                getStringExtra(NotificationsService.NOTIFICATION_DESCRIPTION),
            )
            service.putExtra(
                NotificationsService.NOTIFICATION_ID,
                getIntExtra(NotificationsService.NOTIFICATION_ID, 0),
            )
        }

        context?.let { NotificationsService.enqueueWork(it, service) }
    }
}