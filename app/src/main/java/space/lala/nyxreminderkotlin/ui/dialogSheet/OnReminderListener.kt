package space.lala.nyxreminderkotlin.ui.dialogSheet

import space.lala.nyxreminderkotlin.model.Reminder

public interface OnReminderListener {
    fun onReminderClick(id: Int)

    fun onReminderLongClick(id: Int)

    fun onTimeReminderClick(reminder: Reminder, hour: Int, minute: Int)

    fun onNotificationIconClick(reminder: Reminder)
}