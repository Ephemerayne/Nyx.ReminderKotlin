package com.ephemerayne.reminder.dagger.modules

import android.app.Application
import com.ephemerayne.reminder.datasource.local.database.RemindersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class ReminderDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): RemindersDatabase =
        RemindersDatabase.getDatabase(application)
}