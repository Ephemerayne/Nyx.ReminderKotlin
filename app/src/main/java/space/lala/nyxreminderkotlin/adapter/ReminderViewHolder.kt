package space.lala.nyxreminderkotlin.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ReminderItemBinding
import space.lala.nyxreminderkotlin.model.Reminder

public class ReminderViewHolder(private val binding: ReminderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

//    val reminderId : Int = 0

    fun setItemContent(reminder: Reminder, reminders: ArrayList<Reminder>, position: Int) {

        val date = binding.reminderDate
        val title = binding.reminderTitle
        val description = binding.reminderDescription
        val time = binding.buttonChangeReminderTime
        val cardView = binding.cardViewReminder

        if (position > 0) {
            val previousReminder: Reminder = reminders[position]
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
            cardView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.dark_grey))
        } else {
            cardView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    android.R.color.transparent
                )
            )
        }
    }


}