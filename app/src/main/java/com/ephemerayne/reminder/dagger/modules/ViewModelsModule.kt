package com.ephemerayne.reminder.dagger.modules

import androidx.lifecycle.ViewModel
import com.ephemerayne.reminder.MainActivityViewModel
import com.ephemerayne.reminder.dagger.ViewModelKey
import com.ephemerayne.reminder.ui.dialogSheet.AddEditReminderDialogViewModel
import com.ephemerayne.reminder.ui.dialogSheet.ViewReminderDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun mainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditReminderDialogViewModel::class)
    abstract fun addEditReminderViewModel(viewModel: AddEditReminderDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewReminderDialogViewModel::class)
    abstract fun viewReminderViewModel(viewModel: ViewReminderDialogViewModel): ViewModel
}