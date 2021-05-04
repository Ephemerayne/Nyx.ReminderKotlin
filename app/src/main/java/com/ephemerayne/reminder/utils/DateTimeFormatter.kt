package com.ephemerayne.reminder.utils

import org.threeten.bp.format.DateTimeFormatter

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")