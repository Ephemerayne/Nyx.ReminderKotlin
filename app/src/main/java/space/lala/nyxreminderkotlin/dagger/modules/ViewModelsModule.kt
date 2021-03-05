package space.lala.nyxreminderkotlin.dagger.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import space.lala.nyxreminderkotlin.MainActivityViewModel
import space.lala.nyxreminderkotlin.dagger.ViewModelKey
import space.lala.nyxreminderkotlin.ui.dialogSheet.AddEditReminderDialogViewModel
import space.lala.nyxreminderkotlin.ui.dialogSheet.ViewReminderDialogViewModel

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