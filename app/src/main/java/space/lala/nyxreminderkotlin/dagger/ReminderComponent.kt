package space.lala.nyxreminderkotlin.dagger

import dagger.Component
import space.lala.nyxreminderkotlin.MainActivity
import space.lala.nyxreminderkotlin.dagger.modules.AppModule
import space.lala.nyxreminderkotlin.dagger.modules.ReminderDaoModule
import space.lala.nyxreminderkotlin.dagger.modules.ReminderDatabaseModule
import space.lala.nyxreminderkotlin.dagger.modules.ViewModelsModule
import space.lala.nyxreminderkotlin.ui.dialogSheet.AddEditReminderDialogSheet
import space.lala.nyxreminderkotlin.ui.dialogSheet.ViewReminderDialogSheet
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ReminderDatabaseModule::class,
        ReminderDaoModule::class,
        ViewModelsModule::class,
    ]
)
interface ReminderComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(addEditReminderDialogSheet: AddEditReminderDialogSheet)
    fun inject(viewReminderDialogSheet: ViewReminderDialogSheet)
}