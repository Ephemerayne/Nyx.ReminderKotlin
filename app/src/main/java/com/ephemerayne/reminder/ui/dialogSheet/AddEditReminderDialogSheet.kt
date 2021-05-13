package com.ephemerayne.reminder.ui.dialogSheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.ephemerayne.reminder.App
import com.ephemerayne.reminder.R
import com.ephemerayne.reminder.databinding.AddEditReminderDialogSheetBinding
import com.ephemerayne.reminder.model.Reminder
import com.ephemerayne.reminder.utils.dateFormatter
import com.ephemerayne.reminder.utils.showTimePicker
import com.ephemerayne.reminder.utils.timeFormatter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import javax.inject.Inject

class AddEditReminderDialogSheet : DialogFragment(R.layout.add_edit_reminder_dialog_sheet) {

    @Inject
    lateinit var viewModel: AddEditReminderDialogViewModel

    private lateinit var binding: AddEditReminderDialogSheetBinding
    private var reminderId: Int? = null
    private var reminder: Reminder? = null

    private var reminderDate = LocalDate.now()
    private var reminderTime = LocalTime.now()

    companion object {
        const val ID_KEY = "ID_KEY"

        fun newInstance(id: Int) = AddEditReminderDialogSheet().apply {
            arguments = bundleOf(ID_KEY to id)
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->

            setDate(year, month + 1, day)
        }

    private val timeSetListener =
        TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->

            setTime(hour, minute)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AddEditReminderDialogSheetBinding.inflate(inflater).also { binding = it }.root

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity?.application as App).reminderComponent.inject(this)
        initListeners()
        fillInDefaultDateTime()
        reminderId = arguments?.getInt(ID_KEY)
        reminderId?.let { editReminder(it) }

        context?.let {
            dialog?.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(it, android.R.color.transparent))
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initListeners() {
        binding.buttonCancelEditReminder.setOnClickListener { dismiss() }

        binding.buttonSaveReminder.setOnClickListener {

            val reminderTitle = binding.titleEditText.text.toString()
            if (reminderTitle.trim().isEmpty()) {
                with(binding.titleEditText) {
                    setText("")
                    setHint(R.string.empty_title)
                    setHintTextColor(ContextCompat.getColor(context, R.color.red))
                }
            } else {

                if (reminder == null) {
                    saveReminder(createReminder())
                } else {
                    reminder?.let { updateReminder(it) }
                }

                dismiss()
            }

        }

        binding.editDate.setOnClickListener {
            showDatePicker()
        }

        binding.editTime.setOnClickListener {
            context?.let { context -> showTimePicker(context, timeSetListener) }
        }
    }

    private fun createReminder(): Reminder {
        val reminderTitle = binding.titleEditText.text.toString().trim()
        val reminderDescription = binding.descriptionEditText.text.toString().trim()
        val dateTime: LocalDateTime = LocalDateTime.of(reminderDate, reminderTime)
        return Reminder(reminderTitle, reminderDescription, dateTime)
    }

    private fun saveReminder(reminder: Reminder) =
        viewModel.insertReminder(reminder)

    private fun updateReminder(reminder: Reminder) =
        viewModel.updateReminder(
            reminder.copyWith(
                title = binding.titleEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                dateTime = LocalDateTime.of(reminderDate, reminderTime)
            )
        )

    private fun editReminder(reminderId: Int) {
        viewModel.getReminder(reminderId).observe(this, { reminder ->
            this.reminder = reminder
            reminder?.let {
                reminderDate = reminder.dateTime.toLocalDate()
                reminderTime = reminder.dateTime.toLocalTime()
                binding.titleEditText.setText(reminder.title)
                binding.descriptionEditText.setText(reminder.description)
                binding.editDate.text = reminder.dateTime.toLocalDate().format(dateFormatter)
                binding.editTime.text = reminder.dateTime.toLocalTime().format(timeFormatter)
            }
        })
    }

    private fun fillInDefaultDateTime() {
        val dateString = dateFormatter.format(reminderDate)
        val timeString = timeFormatter.format(reminderTime)

        binding.editDate.text = dateString
        binding.editTime.text = timeString
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePicker() {
        val datePickerDialog: DatePickerDialog? = context?.let { DatePickerDialog(it) }
        datePickerDialog?.setOnDateSetListener(dateSetListener)
        datePickerDialog?.show()
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        val localDate: LocalDate = LocalDate.of(year, month, day)
        binding.editDate.text = dateFormatter.format(localDate)
        reminderDate = localDate
    }

    private fun setTime(hour: Int, minute: Int) {
        val localTime: LocalTime = LocalTime.of(hour, minute)
        binding.editTime.text = timeFormatter.format(localTime)
        reminderTime = localTime
    }

}

