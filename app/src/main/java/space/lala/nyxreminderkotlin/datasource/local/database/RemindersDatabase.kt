package space.lala.nyxreminderkotlin.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import space.lala.nyxreminderkotlin.model.DateTimeConverter
import space.lala.nyxreminderkotlin.model.Reminder

@Database(entities = [Reminder::class], version = 1)
@TypeConverters(DateTimeConverter::class)
public abstract class RemindersDatabase : RoomDatabase() {

    companion object {
        private var instance: RemindersDatabase? = null
        private const val DB_NAME = "reminders.db"

        fun getDatabase(context: Context): RemindersDatabase {
            if (instance == null) {
                synchronized(RemindersDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext, RemindersDatabase::class.java, DB_NAME
                        )
                            .build()
                    }
                }
            }
            return instance!!
        }
    }

    abstract fun reminderDao(): ReminderDao

}
