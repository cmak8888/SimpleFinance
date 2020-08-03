package com.csci448.FP.simplefinance.data.transactions_data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transactions(@PrimaryKey val id: UUID = UUID.randomUUID(),
                        var timestamp: Date = Date(),
                        var title: String = "",
                        var amount: Double = 0.0,
                        var spentOrEarned: String = "",
                        var description: String? = null)