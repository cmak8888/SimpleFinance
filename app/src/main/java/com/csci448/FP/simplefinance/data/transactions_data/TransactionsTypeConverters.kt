package com.csci448.FP.simplefinance.data.transactions_data

import androidx.room.TypeConverter
import java.util.*


class TransactionsTypeConverters {

    @TypeConverter
    fun fromTimestamp(timestamp: Date?): Long? {
        return timestamp?.time
    }

    @TypeConverter
    fun toTimestamp(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

}