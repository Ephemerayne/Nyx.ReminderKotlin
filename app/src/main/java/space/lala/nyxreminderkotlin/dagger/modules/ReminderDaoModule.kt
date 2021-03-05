package space.lala.nyxreminderkotlin.dagger.modules

import dagger.Module
import dagger.Provides
import space.lala.nyxreminderkotlin.datasource.local.database.ReminderDao
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase
import javax.inject.Singleton

@Module
public class  ReminderDaoModule {

    @Singleton
    @Provides
    fun provideDao(database: RemindersDatabase): ReminderDao = database.reminderDao()
}
