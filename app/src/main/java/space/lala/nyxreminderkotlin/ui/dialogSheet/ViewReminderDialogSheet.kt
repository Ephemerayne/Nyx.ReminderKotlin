package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import space.lala.nyxreminderkotlin.App
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ViewReminderDialogSheetBinding
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.utils.dateFormatter
import space.lala.nyxreminderkotlin.utils.timeFormatter
import javax.inject.Inject

class ViewReminderDialogSheet : DialogFragment() {

    @Inject
    lateinit var viewModel: ViewReminderDialogViewModel

    private lateinit var binding: ViewReminderDialogSheetBinding
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
        (activity?.application as App).reminderComponent.inject(this)
        reminderId = arguments?.getInt(ID_KEY)
        reminderId?.let { id ->
            viewModel.getReminder(id).observe(this, { reminder ->
                setReminderData(reminder)
            })
        }
        initListeners()

        context?.let {
            dialog?.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(it, android.R.color.transparent))
            )
        }
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
        binding.viewDialogTitle.text = reminder.title.trim()
        binding.viewDialogDescription.text = reminder.description.trim()

        val dateString = reminder.dateTime.toLocalDate().format(dateFormatter)
        val timeString = reminder.dateTime.toLocalTime().format(timeFormatter)
        binding.viewDateTime.text = getString(R.string.on_date_in_time, dateString, timeString)

        if (reminder.description.isEmpty()) {
            binding.viewDialogDescription.visibility = View.GONE
        }
    }
}