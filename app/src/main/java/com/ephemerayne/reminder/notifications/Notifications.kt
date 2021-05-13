package com.ephemerayne.reminder.notifications

import com.ephemerayne.reminder.model.Reminder

public interface Notifications {
    fun sendNotification(reminder: Reminder)

    fun cancelNotification(id: Int)
}