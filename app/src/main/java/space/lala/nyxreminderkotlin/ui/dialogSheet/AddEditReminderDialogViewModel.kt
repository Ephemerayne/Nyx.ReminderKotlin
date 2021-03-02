package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder

public class AddEditReminderDialogViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository = ReminderRepository(application)

    public fun insertReminder(reminder: Reminder) =
        repository.insertReminder(reminder)

}