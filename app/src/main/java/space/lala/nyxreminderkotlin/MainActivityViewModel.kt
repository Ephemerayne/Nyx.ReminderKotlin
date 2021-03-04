package space.lala.nyxreminderkotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import space.lala.nyxreminderkotlin.datasource.local.database.ReminderDao
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase
import space.lala.nyxreminderkotlin.model.Reminder

public class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val reminderDao : ReminderDao = RemindersDatabase.getDatabase(application).reminderDao()
    val reminderList: LiveData<List<Reminder>> = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) = reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(id: Int) = reminderDao.deleteReminder(id)

    fun getAllReminders() :LiveData<List<Reminder>> = reminderDao.getAllReminders()
}