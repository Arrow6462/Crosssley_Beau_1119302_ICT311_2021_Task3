
package com.example.workoutapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import database.WorkoutDao
import java.util.*

private const val TAG = "WorkoutListFragment"

class WorkoutListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onWorkoutSelected(workoutId: UUID)
    }

    private var callbacks: Callbacks? = null

    // Start-up work for deleting an activity.
    val selectedWorkouts = arrayListOf<Workout>()

    private lateinit var workoutRecyclerView: RecyclerView
    private var adapter: WorkoutAdapter? = WorkoutAdapter(emptyList())
    private val workoutListViewModel: WorkoutListViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_list, container, false)

        workoutRecyclerView =
            view.findViewById(R.id.workout_recycler_view) as RecyclerView
        workoutRecyclerView.layoutManager = LinearLayoutManager(context)
        workoutRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutListViewModel.workoutListLiveData.observe(
            viewLifecycleOwner,
            Observer { workouts ->
                workouts?.let {
                    Log.i(TAG, "Got crimes ${workouts.size}")
                    updateUI(workouts)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_workout_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_workout -> {
                val workout = Workout()
                workoutListViewModel.addWorkout(workout)
                callbacks?.onWorkoutSelected(workout.id)
                Toast.makeText(context, "Workout Created!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(workouts: List<Workout>) {
        adapter = WorkoutAdapter(workouts)
        workoutRecyclerView.adapter = adapter
    }

    private inner class WorkoutHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
            private lateinit var workout: Workout

            private val titleTextView: TextView = itemView.findViewById(R.id.workout_title)
            private val locationTextView: TextView = itemView.findViewById(R.id.workout_location)
            private val dateTextView: TextView = itemView.findViewById(R.id.workout_date)
            private val startTimeTextView: TextView = itemView.findViewById(R.id.workout_start_time)
            private val endTimeTextView: TextView = itemView.findViewById(R.id.workout_end_time)
            private val groupImageView: ImageView = itemView.findViewById(R.id.workout_group_icon)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind (workout: Workout) {
            this.workout = workout
            titleTextView.text = this.workout.title
            locationTextView.text = this.workout.location
            dateTextView.text = android.text.format.DateFormat.format("EEE-dd-MMM-yyyy", this.workout.date)
            startTimeTextView.text = "Start Time: ${this.workout.startTimeHour}" + ":" + "${this.workout.startTimeMinute}"
            endTimeTextView.text = "End Time: ${this.workout.endTimeHour}" + ":" + "${this.workout.endTimeMinute}"
            groupImageView.visibility = if (workout.groupActivity) {
                View.VISIBLE
            }else {
                View.GONE
            }
        }

        override fun onClick(view: View) {
            callbacks?.onWorkoutSelected(workout.id)
        }
    }

    private inner class WorkoutAdapter(var workouts: List<Workout>)
        : RecyclerView.Adapter<WorkoutHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
            val view = layoutInflater.inflate(R.layout.list_item_workout, parent, false)
            return WorkoutHolder(view)
        }

        override fun getItemCount() = workouts.size

        override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
            val workout = workouts[position]
            holder.bind(workout)
            // Floating action button gets set up here???
            // if statment (selectedWorkouts.contains(workout val??? or workout class???))
        }
    }

    companion object{
        fun newInstance(): WorkoutListFragment {
            return WorkoutListFragment()
        }
    }
}