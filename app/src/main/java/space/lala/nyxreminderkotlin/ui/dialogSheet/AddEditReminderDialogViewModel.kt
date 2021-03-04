package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder

public class AddEditReminderDialogViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository = ReminderRepository(application)

    public fun insertReminder(reminder: Reminder) =
        repository.insertReminder(reminder)

    public fun getReminder(id: Int): LiveData<Reminder> = repository.getReminder(id)

    public fun updateReminder(reminder: Reminder) = repository.updateReminder(reminder)

}