package com.ephemerayne.reminder.dagger

import com.ephemerayne.reminder.MainActivity
import com.ephemerayne.reminder.dagger.modules.*
import com.ephemerayne.reminder.ui.dialogSheet.AddEditReminderDialogSheet
import com.ephemerayne.reminder.ui.dialogSheet.ViewReminderDialogSheet
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ReminderDatabaseModule::class,
        ReminderDaoModule::class,
        ViewModelsModule::class,
        NotificationsModule::class
    ]
)
interface ReminderComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(addEditReminderDialogSheet: AddEditReminderDialogSheet)
    fun inject(viewReminderDialogSheet: ViewReminderDialogSheet)
}