package space.lala.nyxreminderkotlin.ui.dialogSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.R
import space.lala.nyxreminderkotlin.databinding.ViewReminderDialogSheetBinding
import space.lala.nyxreminderkotlin.model.Reminder
import java.util.*

class ViewReminderDialogSheet : DialogFragment() {

    private lateinit var binding: ViewReminderDialogSheetBinding


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

        if (arguments !=null) {

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

    private fun setReminderDataForAdd(reminder: Reminder) {
        binding.viewDialogTitle.text = reminder.title
        binding.viewDialogDescription.text = reminder.description

        val dateString: String = android.text.format.DateFormat.format("dd.MM.yy", Date())
            .toString()
        val timeString : String = android.text.format.DateFormat.format("hh.MM", Date())
            .toString()
        binding.viewDateTime.text = getString(R.string.on_date_in_time, dateString, timeString)

        if (reminder.description.isEmpty()) {
            binding.viewDialogDescription.visibility = View.GONE
        }
    }
}