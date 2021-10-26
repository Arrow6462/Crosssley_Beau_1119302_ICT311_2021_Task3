package com.example.workoutapp

import android.text.format.Time
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.Date

@Entity
data class Workout (@PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var location: String = "",
    var groupActivity: Boolean = false,
    var date: Date = Date(),
    var startTimeHour: Int = 0,
    var startTimeMinute: Int = 0,
    var endTimeHour: Int = 0,
    var endTimeMinute: Int = 0
)