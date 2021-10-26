package com.example.workoutapp

import android.app.Application

class WorkoutIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        WorkoutRepository.initialise(this)
    }
}