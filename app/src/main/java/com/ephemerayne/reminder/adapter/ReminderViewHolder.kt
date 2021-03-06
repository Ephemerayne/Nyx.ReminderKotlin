package com.ephemerayne.reminder.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ephemerayne.reminder.R
import com.ephemerayne.reminder.databinding.ReminderItemBinding
import com.ephemerayne.reminder.model.Reminder
import com.ephemerayne.reminder.ui.dialogSheet.OnReminderListener
import com.ephemerayne.reminder.utils.dateFormatter
import com.ephemerayne.reminder.utils.timeFormatter
import org.threeten.bp.LocalDate


class ReminderViewHolder(
    private val binding: ReminderItemBinding,
    private val onReminderListener: OnReminderListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var reminderId: Int? = null

    fun setItemContent(reminder: Reminder, reminders: ArrayList<Reminder>, position: Int) {
        reminderId = reminder.id

        val date = binding.reminderDate
        val title = binding.reminderTitle
        val description = binding.reminderDescription
        val time = binding.buttonChangeReminderTime
        val cardView = binding.cardViewReminder
        val notificationIcon = binding.notificationIcon

        if (position > 0) {
            val previousReminder: Reminder = reminders[position - 1]
            val previousDate: LocalDate = previousReminder.dateTime.toLocalDate()
            val currentDate: LocalDate = reminder.dateTime.toLocalDate()

            if (previousDate.compareTo(currentDate) == 0) {
                date.visibility = View.GONE
            } else {
                date.visibility = View.VISIBLE
            }
        }

        if (position == 0) date.visibility = View.VISIBLE

        val dateString: String = dateFormatter.format(reminder.dateTime)
        val timeString: String = timeFormatter.format(reminder.dateTime)

        title.text = reminder.title.trim()
        description.text = reminder.description.trim()
        date.text = dateString
        time.text = timeString

        if (reminder.isSelected) {
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.dark_grey)
            )
        } else {
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.light_blue
                )
            )
        }

        notificationIcon.setOnClickListener {
            onReminderListener.onNotificationIconClick(reminder)
        }

        cardView.setOnClickListener {
            reminderId?.let { onReminderListener.onReminderClick(it) }
        }

        cardView.setOnLongClickListener {
            onLongClickReminder()
        }

        time.setOnClickListener {
            onReminderListener.onTimeReminderClick(
                reminder,
                reminder.dateTime.hour,
                reminder.dateTime.minute
            )
        }
        setNotificationIcon(reminder)
    }

    private fun onLongClickReminder(): Boolean {
        reminderId?.let { onReminderListener.onReminderLongClick(it) }
        return true
    }

    private fun setNotificationIcon(reminder: Reminder) {
        if (reminder.isNotificationActive) {
            binding.notificationIcon.setImageResource(R.drawable.icon_notification_active)
        } else {
            binding.notificationIcon.setImageResource(R.drawable.icon_notification_inactive)
        }
    }
}