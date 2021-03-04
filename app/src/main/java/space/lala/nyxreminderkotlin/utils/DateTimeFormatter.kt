package space.lala.nyxreminderkotlin.utils

import org.threeten.bp.format.DateTimeFormatter

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")