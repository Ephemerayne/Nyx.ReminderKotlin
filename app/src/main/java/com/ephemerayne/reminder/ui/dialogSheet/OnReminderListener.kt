package com.ephemerayne.reminder.ui.dialogSheet

import com.ephemerayne.reminder.model.Reminder

public interface OnReminderListener {
    fun onReminderClick(id: Int)

    fun onReminderLongClick(id: Int)

    fun onTimeReminderClick(reminder: Reminder, hour: Int, minute: Int)

    fun onNotificationIconClick(reminder: Reminder)
}