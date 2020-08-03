package com.csci448.FP.simplefinance.data.credentials_data

import android.telephony.PhoneNumberUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class AccountCredentials(@PrimaryKey val id: UUID = UUID.randomUUID(),
                              var firstName: String = "",
                              var lastName: String = "",
                              var email: String = "",
                              var password: String = "",
                              var phoneNumber: PhoneNumberUtils? = null)