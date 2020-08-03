package com.csci448.FP.simplefinance.data.credentials_data

import android.telephony.PhoneNumberUtils
import androidx.room.TypeConverter
import java.util.*

class AccountCredentialsTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromPhoneNumber(phoneNumber: PhoneNumberUtils?): String? {
        return phoneNumber.toString()
    }

    @TypeConverter
    fun toPhoneNumber(phoneNumber: String?): PhoneNumberUtils? {
        if(phoneNumber!!.isEmpty()) {
            return toPhoneNumber(null)
        }

        return  toPhoneNumber(phoneNumber)
    }
}