package com.ephemerayne.reminder.datasource.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ephemerayne.reminder.model.Reminder

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders_table WHERE id=:id LIMIT 1")
    fun getReminder(id: Int): LiveData<Reminder>

    @Query("SELECT * FROM reminders_table")
    fun getAllReminders(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders_table")
    fun getAllRemindersSync(): List<Reminder>

    @Query("DELETE FROM reminders_table WHERE id =:id")
    fun deleteReminder(id: Int)

    @Update
    fun updateReminder(reminder: Reminder)
}