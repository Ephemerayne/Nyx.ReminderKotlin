package space.lala.nyxreminderkotlin

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import space.lala.nyxreminderkotlin.adapter.ReminderAdapter
import space.lala.nyxreminderkotlin.databinding.ActivityMainBinding
import space.lala.nyxreminderkotlin.model.Reminder
import space.lala.nyxreminderkotlin.ui.dialogSheet.AddEditReminderDialogSheet
import space.lala.nyxreminderkotlin.ui.dialogSheet.OnReminderListener
import space.lala.nyxreminderkotlin.ui.dialogSheet.ViewReminderDialogSheet
import space.lala.nyxreminderkotlin.utils.notifications.Notifications
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnReminderListener {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var notifications: Notifications

    private lateinit var binding: ActivityMainBinding
    private val adapter: ReminderAdapter = ReminderAdapter(this)

    private lateinit var deleteMenuItem: MenuItem

    private val itemsToRemove = ArrayList<Int>()
    private var isSelectModeActive: Boolean = false

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
            setRemindersNotifications(reminders)

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
            disableSelectMode()
        }

        binding.addReminderButton.setOnClickListener {
            openAddEditReminderDialog()
            disableSelectMode()
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
        showDeleteButton(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_trash_basket -> {
                deleteReminders()
                disableSelectMode()
            }
            else -> return false
        }
        return true
    }

    override fun onReminderClick(id: Int) {
        if (!isSelectModeActive) {
            openViewReminderDialog(id)
            return
        }
        val reminders: ArrayList<Reminder> = ArrayList(adapter.getReminders())
        for (reminder: Reminder in reminders) {
            if (isSelectModeActive && reminder.id == id) {
                reminder.isSelected = !reminder.isSelected
                manageItemsToRemove(reminder)
                setSelectModeActive()
            }
        }
        adapter.setReminders(reminders)
        showDeleteButton(isSelectModeActive)
    }

    override fun onReminderLongClick(id: Int) {
        val reminders: ArrayList<Reminder> = ArrayList(adapter.getReminders())
        for (reminder: Reminder in reminders) {
            if (reminder.id == id) {
                reminder.isSelected = !reminder.isSelected
                manageItemsToRemove(reminder)
                setSelectModeActive()
            }
        }
        adapter.setReminders(reminders)
        showDeleteButton(isSelectModeActive)
    }

    override fun onTimeReminderClick(reminder: Reminder, hour: Int, minute: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val localTime = LocalTime.of(hour, minute)
            val dateTime = LocalDateTime.of(reminder.dateTime.toLocalDate(), localTime)
            viewModel.updateReminder(reminder.copyWith(dateTime = dateTime))
        }
    }

    override fun onNotificationIconClick(reminder: Reminder) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateReminder(
                reminder.copyWith(isNotificationActive = !reminder.isNotificationActive)
            )
        }
    }

    private fun showDeleteButton(willShow: Boolean) {
        deleteMenuItem.isVisible = willShow
        deleteMenuItem.isEnabled = willShow
    }

    private fun setSelectModeActive() {
        isSelectModeActive = itemsToRemove.isNotEmpty()
    }

    private fun manageItemsToRemove(reminder: Reminder) {
        if (reminder.isSelected) {
            reminder.id?.let { itemsToRemove.add(it) }
        } else {
            itemsToRemove.remove(reminder.id)
        }
    }

    private fun deleteReminders() {
        CoroutineScope(Dispatchers.IO).launch {
            for (id: Int in itemsToRemove) {
                viewModel.deleteReminder(id)
                notifications.cancelNotification(id)
            }
        }
    }

    private fun disableSelectMode() {
        setSelectModeActive()
        showDeleteButton(false)

        val reminders: ArrayList<Reminder> = ArrayList(adapter.getReminders())
        for (reminder: Reminder in reminders) {
            if (reminder.isSelected) {
                reminder.isSelected = !reminder.isSelected
            }
        }
        adapter.setReminders(reminders)
    }

    private fun setRemindersNotifications(reminders: List<Reminder>) =
        reminders
            .filter { it.dateTime.isAfter(LocalDateTime.now()) }
            .forEach { notifications.sendNotification(it) }
}