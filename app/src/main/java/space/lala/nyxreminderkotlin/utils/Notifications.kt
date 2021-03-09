package space.lala.nyxreminderkotlin.utils

import android.content.Context
import space.lala.nyxreminderkotlin.model.Reminder

public interface Notifications {

    fun createNotificationChannel(context: Context)

    fun sendNotification(context: Context, reminder: Reminder)
}