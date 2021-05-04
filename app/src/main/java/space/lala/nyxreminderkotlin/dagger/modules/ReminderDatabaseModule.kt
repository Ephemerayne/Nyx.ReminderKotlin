package space.lala.nyxreminderkotlin.dagger.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase
import javax.inject.Singleton

@Module
public class ReminderDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): RemindersDatabase =
        RemindersDatabase.getDatabase(application)
}