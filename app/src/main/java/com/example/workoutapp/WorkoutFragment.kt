package com.example.workoutapp

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import java.util.*

private const val TAG = "WorkoutFragment"
private const val ARG_WORKOUT_ID = "workout_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class WorkoutFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerDialog.OnTimeSetListener {
    private lateinit var workout: Workout
    private lateinit var titleField: EditText
    private lateinit var locationField: EditText
    private lateinit var dateButton: Button
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button
    private lateinit var groupActivityCheckBox: CheckBox
    private var typeOfTimePicked: String = ""

    private val workoutDetailViewModel: WorkoutDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workout = Workout()

        val workoutId: UUID = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        workoutDetailViewModel.loadWorkout(workoutId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_workout, container, false)

        titleField = view.findViewById(R.id.workout_title) as EditText
        locationField = view.findViewById(R.id.workout_location) as EditText
        dateButton = view.findViewById(R.id.workout_date) as Button
        startTimeButton = view.findViewById(R.id.workout_start_time_button) as Button
        endTimeButton = view.findViewById(R.id.workout_end_time_button) as Button
        groupActivityCheckBox = view.findViewById(R.id.group_activity) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutDetailViewModel.workoutLiveData.observe(
            viewLifecycleOwner,
            Observer { workout ->
                workout?.let {
                    this.workout = workout
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        // A Listener for the editable text field of the workout activity title.
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int)
            {
                // This space intentionally left blank.
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int)
            {
                workout.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        // A Listener for the editable text field of the workout activity location.
        val locationWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int)
            {
                // This space intentionally left blank.
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int)
            {
                workout.location = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        locationField.addTextChangedListener(locationWatcher)

        groupActivityCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                workout.groupActivity = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(workout.date).apply {
                setTargetFragment(this@WorkoutFragment, REQUEST_DATE)
                show(this@WorkoutFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        // Time Picker Buttons.
        startTimeButton.setOnClickListener {
            typeOfTimePicked = "Start"
            pickTime()
        }

        endTimeButton.setOnClickListener {
            typeOfTimePicked = "End"
            pickTime()
        }
    }

    private fun pickTime() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        TimePickerDialog(context, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (typeOfTimePicked == "Start") {
            workout.startTimeHour = hourOfDay
            workout.startTimeMinute = minute
        }
        else if (typeOfTimePicked == "End") {
            workout.endTimeHour = hourOfDay
            workout.endTimeMinute = minute
        }
        updateUI()
    }

    override fun onStop() {
        super.onStop()
        workoutDetailViewModel.saveWorkout(workout)
        //Toast.makeText(context, "Workout Created / Updated!", Toast.LENGTH_SHORT).show()
    }

    override fun onDateSelected(date: Date) {
        workout.date = date
        updateUI()
    }

    // Add the time picker here later on.
    private fun updateUI() {
        titleField.setText(workout.title)
        locationField.setText(workout.location)
        dateButton.text = workout.date.toString()
        startTimeButton.text = "Start Time: " + workout.startTimeHour.toString() + ":" + workout.startTimeMinute.toString()
        endTimeButton.text = "End Time: " + workout.endTimeHour.toString() + ":" + workout.endTimeMinute.toString()
        groupActivityCheckBox.apply {
            isChecked = workout.groupActivity // Double check this too!!
            jumpDrawablesToCurrentState()
        }
    }

    companion object{
        fun newInstance(workoutId: UUID): WorkoutFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WORKOUT_ID, workoutId)
            }
            return  WorkoutFragment().apply {
                arguments = args
            }
        }
    }
}