package com.ephemerayne.reminder.dagger.modules

import android.app.Application
import com.ephemerayne.reminder.notifications.Notifications
import com.ephemerayne.reminder.notifications.NotificationsImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class NotificationsModule {

    @Singleton
    @Provides
    fun provideNotification(application: Application): Notifications =
        NotificationsImpl(application)
}