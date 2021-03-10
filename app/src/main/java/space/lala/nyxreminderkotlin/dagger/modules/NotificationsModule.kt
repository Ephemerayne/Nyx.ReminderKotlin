package space.lala.nyxreminderkotlin.dagger.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import space.lala.nyxreminderkotlin.utils.notifications.Notifications
import space.lala.nyxreminderkotlin.utils.notifications.NotificationsImpl
import javax.inject.Singleton

@Module
public class NotificationsModule {

    @Singleton
    @Provides
    fun provideNotification(application: Application): Notifications =
        NotificationsImpl(application)
}