package space.lala.nyxreminderkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder
import javax.inject.Inject

public class MainActivityViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {
    fun updateReminder(reminder: Reminder) = repository.updateReminder(reminder)

    fun deleteReminder(id: Int) = repository.deleteReminder(id)

    fun getAllReminders(): LiveData<List<Reminder>> = repository.getAllReminders()
}