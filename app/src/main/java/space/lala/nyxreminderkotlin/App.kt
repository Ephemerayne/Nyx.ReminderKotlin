package space.lala.nyxreminderkotlin

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import space.lala.nyxreminderkotlin.dagger.DaggerReminderComponent
import space.lala.nyxreminderkotlin.dagger.ReminderComponent
import space.lala.nyxreminderkotlin.dagger.modules.AppModule

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