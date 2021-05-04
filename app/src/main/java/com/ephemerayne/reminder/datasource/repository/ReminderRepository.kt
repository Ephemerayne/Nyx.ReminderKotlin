package com.ephemerayne.reminder.datasource.repository

import androidx.lifecycle.LiveData
import com.ephemerayne.reminder.datasource.local.database.ReminderDao
import com.ephemerayne.reminder.model.Reminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val reminderDao: ReminderDao) {
    fun insertReminder(reminder: Reminder) = CoroutineScope(Dispatchers.IO).launch {
        reminderDao.insertReminder(reminder)
    }

    fun updateReminder(reminder: Reminder) = CoroutineScope(Dispatchers.IO).launch {
        reminderDao.updateReminder(reminder)
    }

    fun getReminder(id: Int): LiveData<Reminder> {
        return reminderDao.getReminder(id)
    }

    fun getAllReminders(): LiveData<List<Reminder>> {
        return reminderDao.getAllReminders()
    }

    fun getAllRemindersSync(): List<Reminder> = reminderDao.getAllRemindersSync()

    fun deleteReminder(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        reminderDao.deleteReminder(id)
    }
}