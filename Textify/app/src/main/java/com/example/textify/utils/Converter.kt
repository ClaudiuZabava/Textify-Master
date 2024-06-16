package com.example.textify.utils

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.*
object Converters {
    @TypeConverter
    fun timestampToDatestamp(timestamp: Timestamp): Long {
        return timestamp.toDate().time
    }

    @TypeConverter
    fun datestampToTimestamp(value: Long): Timestamp {
        return Timestamp(Date(value))
    }
}