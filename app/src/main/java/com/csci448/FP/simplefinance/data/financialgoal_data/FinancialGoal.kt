package com.csci448.FP.simplefinance.data.financialgoal_data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.FileDescriptor
import java.time.LocalDate
import java.util.*

@Entity
data class FinancialGoal(@PrimaryKey val id: UUID = UUID.randomUUID(),
                         var deadline: String = "",
                         var goalAmount: Double = 0.0,
                         var currentAmount: Double = 0.0,
                         var title: String = "",
                         var description: String = "",
                         var frequency: String? = "")