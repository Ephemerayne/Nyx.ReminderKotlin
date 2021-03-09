package space.lala.nyxreminderkotlin.utils.notifications

import space.lala.nyxreminderkotlin.model.Reminder

public interface Notifications {

    fun createNotificationChannel()

    fun sendNotification(reminder: Reminder)
}