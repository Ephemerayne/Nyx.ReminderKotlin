package space.lala.nyxreminderkotlin.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ReminderItemBinding
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.ui.dialogSheet.OnReminderListener

public class ReminderViewHolder(
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

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateString: String = dateFormatter.format(reminder.dateTime)
        val timeString: String = timeFormatter.format(reminder.dateTime)

        title.text = reminder.title
        description.text = reminder.description
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

        binding.cardViewReminder.setOnClickListener {
            reminderId?.let { onReminderListener.onReminderClick(it) }
        }

        binding.cardViewReminder.setOnLongClickListener {
            onLongClickReminder()
        }
    }

    private fun onLongClickReminder(): Boolean {
        println("debugg: $reminderId")
        reminderId?.let { onReminderListener.onReminderLongClick(it) }
        return true
    }
}