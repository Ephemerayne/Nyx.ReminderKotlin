package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.threeten.bp.format.DateTimeFormatter
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ViewReminderDialogSheetBinding
import space.lala.nyxreminderkotlin.model.Reminder

class ViewReminderDialogSheet : DialogFragment() {

    private lateinit var binding: ViewReminderDialogSheetBinding
    private lateinit var viewModel: ViewReminderDialogViewModel

    companion object {
        public const val ID_KEY = "ID_KEY"

        fun newInstance(id: Int) = ViewReminderDialogSheet().apply {
            arguments = bundleOf(ID_KEY to id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewReminderDialogSheetBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ViewReminderDialogViewModel::class.java)
        val reminderId: Int? = arguments?.getInt(ID_KEY)
        reminderId?.let { id ->
            viewModel.getReminder(id).observe(this, { reminder ->
                setReminderData(reminder)
            })
        }
        initListeners()
    }

    private fun initListeners() {
        binding.viewDialogButtonEdit.setOnClickListener {
            dismiss()
            (activity as MainActivity).openAddEditReminderDialog(id)
        }
        binding.closeButtonViewDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun setReminderData(reminder: Reminder) {
        binding.viewDialogTitle.text = reminder.title
        binding.viewDialogDescription.text = reminder.description

        val dateString =
            reminder.dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yy"))
        val timeString =
            reminder.dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        binding.viewDateTime.text = getString(R.string.on_date_in_time, dateString, timeString)

        if (reminder.description.isEmpty()) {
            binding.viewDialogDescription.visibility = View.GONE
        }
    }
}