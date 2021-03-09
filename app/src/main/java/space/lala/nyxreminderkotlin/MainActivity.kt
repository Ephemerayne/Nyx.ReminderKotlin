package space.lala.nyxreminderkotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
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
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnReminderListener {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding
    private val adapter: ReminderAdapter = ReminderAdapter(this)

    private lateinit var deleteMenuItem: MenuItem

    private val itemsToRemove = ArrayList<Int>()
    private var isSelectModeActive: Boolean = false

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 1
    }


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

        createNotificationChannel()
        sendNotification()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TITLE"
            val description = "DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }
            val notificationManager: NotificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Title")
            .setContentText("description")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBB"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
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
}