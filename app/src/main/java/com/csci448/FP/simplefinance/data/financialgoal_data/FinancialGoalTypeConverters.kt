package com.csci448.FP.simplefinance.data.financialgoal_data

import androidx.room.TypeConverter
import java.util.*

class FinancialGoalTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

}