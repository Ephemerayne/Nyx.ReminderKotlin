package com.ephemerayne.reminder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ephemerayne.reminder.adapter.ReminderAdapter
import com.ephemerayne.reminder.databinding.ActivityMainBinding
import com.ephemerayne.reminder.model.Reminder
import com.ephemerayne.reminder.ui.dialogSheet.AddEditReminderDialogSheet
import com.ephemerayne.reminder.ui.dialogSheet.OnReminderListener
import com.ephemerayne.reminder.ui.dialogSheet.ViewReminderDialogSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnReminderListener {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding
    private val adapter: ReminderAdapter = ReminderAdapter(this)

    private var deleteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).reminderComponent.inject(this)

        ActivityMainBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }

        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(this)

        binding.recyclerViewReminders.adapter = adapter

        viewModel.getAllReminders().observe(this, { reminders ->
            viewModel.setRemindersNotifications(reminders)

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
            openAddEditReminderDialog()
            viewModel.disableSelectMode()
        }

        binding.addReminderButton.setOnClickListener {
            openAddEditReminderDialog()
        }

        binding.recyclerViewReminders.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.floatingButtonAddReminder.isShown) {
                    binding.floatingButtonAddReminder.hide()
                }
            }

            @Override
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.floatingButtonAddReminder.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        viewModel.isSelectModeActive.observe(this, { willShow ->
            deleteMenuItem?.isVisible = willShow
            deleteMenuItem?.isEnabled = willShow
        })
    }

    private fun openAddEditReminderDialog() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val addReminderDialog: DialogFragment = AddEditReminderDialogSheet()
        addReminderDialog.show(fragmentTransaction, "ADD")
    }

    public fun openAddEditReminderDialog(id: Int) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        AddEditReminderDialogSheet.newInstance(id).show(fragmentTransaction, "EDIT")
    }

    private fun openViewReminderDialog(id: Int) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        ViewReminderDialogSheet.newInstance(id).show(fragmentTransaction, "OPEN")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_icon_trashbasket, menu)
        deleteMenuItem = menu.findItem(R.id.icon_trash_basket)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_trash_basket -> viewModel.deleteReminders()
            else -> return false
        }
        return true
    }

    override fun onReminderClick(id: Int) {
        if (viewModel.isSelectModeActive.value == false) {
            openViewReminderDialog(id)
            return
        }
        val reminders: ArrayList<Reminder> = ArrayList(adapter.getReminders())
        for (reminder: Reminder in reminders) {
            if (viewModel.isSelectModeActive.value == true && reminder.id == id) {
                reminder.isSelected = !reminder.isSelected
                viewModel.manageItemsToRemove(reminder)
                viewModel.setSelectModeActive()
            }
        }
        adapter.setReminders(reminders)
    }

    override fun onReminderLongClick(id: Int) {
        val reminders: ArrayList<Reminder> = ArrayList(adapter.getReminders())
        for (reminder: Reminder in reminders) {
            if (reminder.id == id) {
                reminder.isSelected = !reminder.isSelected
                viewModel.manageItemsToRemove(reminder)
                viewModel.setSelectModeActive()
            }
        }
        adapter.setReminders(reminders)
    }

    override fun onTimeReminderClick(reminder: Reminder, hour: Int, minute: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val localTime = LocalTime.of(hour, minute)
            val dateTime = LocalDateTime.of(reminder.dateTime.toLocalDate(), localTime)
            viewModel.updateReminder(reminder.copyWith(dateTime = dateTime))
        }
    }

    override fun onNotificationIconClick(reminder: Reminder) {
        if (viewModel.isSelectModeActive.value == true) return
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateReminder(
                reminder.copyWith(
                    isNotificationActive = !reminder.isNotificationActive
                ).apply {
                    if (!isNotificationActive) {
                        id?.let { viewModel.cancelNotification(it) }
                    }
                }
            )
        }
    }
}
