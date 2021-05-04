package com.ephemerayne.reminder

import android.app.Application
import com.ephemerayne.reminder.dagger.DaggerReminderComponent
import com.ephemerayne.reminder.dagger.ReminderComponent
import com.ephemerayne.reminder.dagger.modules.AppModule
import com.jakewharton.threetenabp.AndroidThreeTen

class App : Application() {
    lateinit var reminderComponent: ReminderComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        reminderComponent = initReminderComponent()
    }

    private fun initReminderComponent(): ReminderComponent = DaggerReminderComponent.builder()
        .appModule(AppModule(this))
        .build()
}