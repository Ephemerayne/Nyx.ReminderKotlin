package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.AddEditReminderDialogSheetBinding
import space.lala.nyxreminderkotlin.datasource.repository.ReminderRepository
import space.lala.nyxreminderkotlin.model.Reminder

class AddEditReminderDialogSheet : DialogFragment(R.layout.add_edit_reminder_dialog_sheet) {

    private lateinit var binding: AddEditReminderDialogSheetBinding

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH.mm")

    private var reminderDate = LocalDate.now()
    private var reminderTime = LocalTime.now()

    private lateinit var viewModel: AddEditReminderDialogViewModel

    companion object {
        public const val ID_KEY = "ID_KEY"

        fun newInstance(id: Int) = ViewReminderDialogSheet().apply {
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
        initListeners()
        fillInDefaultDateTime()
        viewModel = ViewModelProvider(this).get(AddEditReminderDialogViewModel::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initListeners() {
        binding.buttonCancelEditReminder.setOnClickListener { dismiss() }

        binding.buttonSaveReminder.setOnClickListener {
                saveReminder(createReminder())
        }

        binding.editDate.setOnClickListener {
            showDatePicker()
        }

        binding.editTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun createReminder() : Reminder {
        val reminderTitle = binding.titleEditText.text.toString()
        val reminderDescription = binding.descriptionEditText.text.toString()
        val dateTime : LocalDateTime = LocalDateTime.of(reminderDate, reminderTime)
        return Reminder(reminderTitle, reminderDescription, dateTime)
    }

    private fun saveReminder(reminder : Reminder) {
        viewModel.insertReminder(reminder)
        dismiss()

    }

    private fun fillInDefaultDateTime() {
//        val localDateTime : LocalDateTime = LocalDateTime.now()

        val dateString = dateFormatter.format(reminderDate)
        val timeString = timeFormatter.format(reminderTime)

        binding.editDate.text = dateString
        binding.editTime.text = timeString
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePicker() {
       val datePickerDialog : DatePickerDialog? = context?.let { DatePickerDialog(it) }
        datePickerDialog?.setOnDateSetListener(dateSetListener)
        datePickerDialog?.show()
    }

    private fun setDate(year: Int, month: Int, day: Int) {
       val localDate : LocalDate = LocalDate.of(year, month, day)
        binding.editDate.text = dateFormatter.format(localDate)
        reminderDate = localDate
    }

    private fun showTimePicker() {
        val localTime : LocalTime = LocalTime.now()
        val hour : Int = localTime.hour
        val minute : Int = localTime.minute

        val timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            hour,
            minute,
            true)
        timePickerDialog.show()
    }

    private fun setTime(hour : Int, minute : Int) {
        val localTime : LocalTime = LocalTime.of(hour, minute)
        binding.editTime.text = timeFormatter.format(localTime)
        reminderTime = localTime
    }

}

