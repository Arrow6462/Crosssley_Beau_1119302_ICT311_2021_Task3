package com.example.workoutapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutListViewModel : ViewModel() {

    private val workoutRepository = WorkoutRepository.get()
    val workoutListLiveData = workoutRepository.getWorkouts()

    fun addWorkout(workout: Workout) {
        workoutRepository.addWorkout(workout)
    }

    fun workoutDelete(workout: Workout){
        workoutRepository.workoutDelete(workout)
    }
}