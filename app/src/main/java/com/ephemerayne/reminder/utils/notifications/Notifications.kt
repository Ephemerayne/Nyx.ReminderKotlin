package com.ephemerayne.reminder.utils.notifications

import com.ephemerayne.reminder.model.Reminder

public interface Notifications {
    fun sendNotification(reminder: Reminder)

    fun cancelNotification(id: Int)
}