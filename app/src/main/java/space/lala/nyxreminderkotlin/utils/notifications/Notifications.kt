package space.lala.nyxreminderkotlin.utils.notifications

import space.lala.nyxreminderkotlin.model.Reminder

public interface Notifications {
    fun sendNotification(reminder: Reminder)

    fun cancelNotification(id: Int)
}