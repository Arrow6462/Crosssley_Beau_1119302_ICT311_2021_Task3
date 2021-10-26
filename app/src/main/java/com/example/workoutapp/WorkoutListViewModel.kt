package com.example.workoutapp

import androidx.lifecycle.ViewModel

class WorkoutListViewModel : ViewModel() {

    private val workoutRepository = WorkoutRepository.get()
    val workoutListLiveData = workoutRepository.getWorkouts()

    fun addWorkout(workout: Workout) {
        workoutRepository.addWorkout(workout)
    }
}