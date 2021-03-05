package space.lala.nyxreminderkotlin.ui.dialogSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder
import javax.inject.Inject

public class ViewReminderDialogViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {
    public fun getReminder(id: Int): LiveData<Reminder> = repository.getReminder(id)
}