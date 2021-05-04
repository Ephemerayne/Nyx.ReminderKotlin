package com.ephemerayne.reminder.dagger.modules

import com.ephemerayne.reminder.datasource.local.database.ReminderDao
import com.ephemerayne.reminder.datasource.local.database.RemindersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class  ReminderDaoModule {

    @Singleton
    @Provides
    fun provideDao(database: RemindersDatabase): ReminderDao = database.reminderDao()
}
