package space.lala.nyxreminderkotlin.model

import java.time.LocalDateTime

data class Reminder(
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,
    var id: Int,
    var isSelected: Boolean = false
)
