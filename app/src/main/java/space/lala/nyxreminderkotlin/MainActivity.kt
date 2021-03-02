package space.lala.nyxreminderkotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import space.lala.nyxreminderkotlin.adapter.ReminderAdapter
import space.lala.nyxreminderkotlin.databinding.ActivityMainBinding
import space.lala.nyxreminderkotlin.ui.dialogSheet.AddEditReminderDialogSheet

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter: ReminderAdapter = ReminderAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(this)

        binding.recyclerViewReminders.adapter = adapter

        viewModel.getAllReminders().observe(this, { reminders ->
            if (reminders.isNotEmpty()) {
                binding.recyclerViewReminders.visibility = View.VISIBLE
                binding.addReminderButton.visibility = View.GONE
                binding.floatingButtonAddReminder.visibility = View.VISIBLE
            } else {
                binding.recyclerViewReminders.visibility = View.GONE
                binding.addReminderButton.visibility = View.VISIBLE
                binding.floatingButtonAddReminder.visibility = View.GONE
            }
            adapter.setReminders(reminders = reminders)
        })

        binding.floatingButtonAddReminder.setOnClickListener {
            openAddReminderDialog()
        }

        binding.addReminderButton.setOnClickListener { openAddReminderDialog() }
    }

    private fun openAddReminderDialog() {
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        val addReminderFragment : DialogFragment = AddEditReminderDialogSheet()
        addReminderFragment.show(fragmentTransaction, "ADD")
    }
}