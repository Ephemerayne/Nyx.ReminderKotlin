package space.lala.nyxreminderkotlin.datasource.repository

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase
import space.lala.nyxreminderkotlin.model.Reminder

class ReminderRepository(
    application: Application
) {
    private val reminderDao = RemindersDatabase.getDatabase(application).reminderDao()
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

    fun deleteReminder(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        reminderDao.deleteReminder(id)
    }
}