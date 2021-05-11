package com.ephemerayne.reminder.ui.dialogSheet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.ephemerayne.reminder.App
import com.ephemerayne.reminder.MainActivity
import com.ephemerayne.reminder.R
import com.ephemerayne.reminder.databinding.ViewReminderDialogSheetBinding
import com.ephemerayne.reminder.model.Reminder
import com.ephemerayne.reminder.utils.dateFormatter
import com.ephemerayne.reminder.utils.timeFormatter
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

        if (reminder.description.isNotEmpty()) {
            binding.viewDialogDescription.visibility = View.VISIBLE
        }
    }
}