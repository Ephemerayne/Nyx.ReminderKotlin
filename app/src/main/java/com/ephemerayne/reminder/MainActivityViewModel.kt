package com.ephemerayne.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ephemerayne.reminder.datasource.repository.ReminderRepository
import com.ephemerayne.reminder.model.Reminder
import com.ephemerayne.reminder.utils.notifications.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

public class MainActivityViewModel @Inject constructor(
    private val repository: ReminderRepository,
    private val notifications: Notifications
) : ViewModel() {

    private val itemsToRemove = ArrayList<Int>()
    val isSelectModeActive = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun updateReminder(reminder: Reminder) = repository.updateReminder(reminder)

    private fun deleteReminder(id: Int) = repository.deleteReminder(id)

    fun getAllReminders(): LiveData<List<Reminder>> = repository.getAllReminders()

    fun setSelectModeActive() {
        isSelectModeActive.value = itemsToRemove.isNotEmpty()
    }

    fun manageItemsToRemove(reminder: Reminder) {
        if (reminder.isSelected) {
            reminder.id?.let { itemsToRemove.add(it) }
        } else {
            itemsToRemove.remove(reminder.id)
        }
    }

    fun cancelNotification(id: Int) = notifications.cancelNotification(id)

    fun deleteReminders() {
        CoroutineScope(Dispatchers.IO).launch {
            for (id: Int in itemsToRemove) {
                deleteReminder(id)
                notifications.cancelNotification(id)
            }
            disableSelectMode()
        }
    }

    fun disableSelectMode() {
        CoroutineScope(Dispatchers.IO).launch {
            val reminders = repository.getAllRemindersSync()
            for (reminder: Reminder in reminders) {
                if (reminder.isSelected) {
                    reminder.isSelected = !reminder.isSelected
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                setSelectModeActive()
            }
        }

        itemsToRemove.clear()
    }

    fun setRemindersNotifications(reminders: List<Reminder>) =
        reminders
            .filter { it.dateTime.isAfter(LocalDateTime.now()) && it.isNotificationActive }
            .forEach { notifications.sendNotification(it) }
}
