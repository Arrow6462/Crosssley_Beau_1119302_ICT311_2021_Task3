package com.example.workoutapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class WorkoutDetailViewModel() : ViewModel() {

    private val workoutRepository = WorkoutRepository.get()
    private val workoutIdLiveData = MutableLiveData<UUID>()

    var workoutLiveData: LiveData<Workout?> =
        Transformations.switchMap(workoutIdLiveData) { workoutId ->
            workoutRepository.getWorkout((workoutId))
        }

    fun loadWorkout(workoutId: UUID) {
        workoutIdLiveData.value = workoutId
    }

    fun saveWorkout(workout: Workout) {
        workoutRepository.updateWorkout(workout)
    }
}