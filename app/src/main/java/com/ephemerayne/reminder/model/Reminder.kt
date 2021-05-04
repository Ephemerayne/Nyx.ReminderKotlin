package com.ephemerayne.reminder.model


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

@Entity(
    tableName = "reminders_table", indices = [
        Index(
            value = ["id"],
            unique = true
        )]
)
data class Reminder(
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var isSelected: Boolean = false,
    var isNotificationActive: Boolean = true
) {
    fun copyWith(
        title: String? = null,
        description: String? = null,
        dateTime: LocalDateTime? = null,
        isNotificationActive: Boolean? = null
    ) = Reminder(
        title = title ?: this.title,
        description = description ?: this.description,
        dateTime = dateTime ?: this.dateTime,
        id = id,
        isNotificationActive = isNotificationActive ?: this.isNotificationActive
    )
}

class DateTimeConverter {
    @TypeConverter
    fun toTimeStamp(dateTime: LocalDateTime?): Long? =
        dateTime?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? =
        value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
}
