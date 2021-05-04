package com.ephemerayne.reminder.ui.dialogSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ephemerayne.reminder.datasource.repository.ReminderRepository
import com.ephemerayne.reminder.model.Reminder
import javax.inject.Inject

public class ViewReminderDialogViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {
    public fun getReminder(id: Int): LiveData<Reminder> = repository.getReminder(id)
}