package space.lala.nyxreminderkotlin.ui.dialogSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder
import javax.inject.Inject

public class AddEditReminderDialogViewModel @Inject constructor(
    private val repository: ReminderRepository,
) : ViewModel() {
    public fun insertReminder(reminder: Reminder) =
        repository.insertReminder(reminder)

    public fun getReminder(id: Int): LiveData<Reminder> = repository.getReminder(id)

    public fun updateReminder(reminder: Reminder) = repository.updateReminder(reminder)
}