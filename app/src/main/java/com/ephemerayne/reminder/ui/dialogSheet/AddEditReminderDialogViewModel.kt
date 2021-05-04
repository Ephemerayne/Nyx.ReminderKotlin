package com.ephemerayne.reminder.ui.dialogSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ephemerayne.reminder.datasource.repository.ReminderRepository
import com.ephemerayne.reminder.model.Reminder
import javax.inject.Inject

public class AddEditReminderDialogViewModel @Inject constructor(
    private val repository: ReminderRepository,
) : ViewModel() {
    public fun insertReminder(reminder: Reminder) =
        repository.insertReminder(reminder)

    public fun getReminder(id: Int): LiveData<Reminder> = repository.getReminder(id)

    public fun updateReminder(reminder: Reminder) = repository.updateReminder(reminder)
}