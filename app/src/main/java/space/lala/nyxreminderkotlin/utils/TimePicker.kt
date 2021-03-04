package space.lala.nyxreminderkotlin.utils

import android.app.TimePickerDialog
import android.content.Context
import org.threeten.bp.LocalTime

fun showTimePicker(context: Context, timeSetListener: TimePickerDialog.OnTimeSetListener) {
    val localTime = LocalTime.now()
    val hour = localTime.hour
    val minute = localTime.minute

    val timePickerDialog = TimePickerDialog(
        context,
        timeSetListener,
        hour,
        minute,
        true
    )

    timePickerDialog.show()
}
