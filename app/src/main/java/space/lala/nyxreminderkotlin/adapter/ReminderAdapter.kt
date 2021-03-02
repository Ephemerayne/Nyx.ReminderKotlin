package space.lala.nyxreminderkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.lala.nyxreminderkotlin.databinding.ReminderItemBinding
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.ui.dialogSheet.OnReminderListener

class ReminderAdapter(
    private val onReminderListener: OnReminderListener
) : RecyclerView.Adapter<ReminderViewHolder>() {

    private val reminders: ArrayList<Reminder> = ArrayList()

    fun setReminders(reminders: List<Reminder>) {
        this.reminders.clear()
        this.reminders.addAll(reminders)
        this.reminders.sortBy { reminder -> reminder.dateTime }
        notifyDataSetChanged()
    }

    fun getReminders(): List<Reminder> {
        return reminders
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ReminderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReminderViewHolder(binding, onReminderListener)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder: Reminder = reminders[position]
        holder.setItemContent(reminder, reminders, position)
    }

    override fun getItemCount(): Int {
        return reminders.size
    }
}