package space.lala.nyxreminderkotlin.dagger.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import space.lala.nyxreminderkotlin.datasource.local.database.RemindersDatabase
import javax.inject.Singleton

@Module
public class ReminderDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): RemindersDatabase =
        Room.databaseBuilder(
            application,
            RemindersDatabase::class.java,
            "reminders_table"
        ).build()
}