package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ViewReminderDialogSheetBinding
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.utils.dateFormatter
import space.lala.nyxreminderkotlin.utils.timeFormatter

class ViewReminderDialogSheet : DialogFragment() {

    private lateinit var binding: ViewReminderDialogSheetBinding
    private lateinit var viewModel: ViewReminderDialogViewModel
    private var reminderId: Int? = null

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
        reminderId = arguments?.getInt(ID_KEY)
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
            reminderId?.let {
                (activity as MainActivity).openAddEditReminderDialog(it)
            }
        }
        binding.closeButtonViewDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun setReminderData(reminder: Reminder) {
        binding.viewDialogTitle.text = reminder.title
        binding.viewDialogDescription.text = reminder.description

        val dateString = reminder.dateTime.toLocalDate().format(dateFormatter)
        val timeString = reminder.dateTime.toLocalTime().format(timeFormatter)
        binding.viewDateTime.text = getString(R.string.on_date_in_time, dateString, timeString)

        if (reminder.description.isEmpty()) {
            binding.viewDialogDescription.visibility = View.GONE
        }
    }
}