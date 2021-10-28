package com.example.workoutapp

import android.app.Application
import androidx.lifecycle.*
import java.util.*

class WorkoutDetailViewModel(app: Application) : AndroidViewModel(app) {

    private val workoutRepository = WorkoutRepository.get()
    private val workoutIdLiveData = MutableLiveData<UUID>()

    // val workoutList = database?.WorkoutDao()?.getWorkouts()

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