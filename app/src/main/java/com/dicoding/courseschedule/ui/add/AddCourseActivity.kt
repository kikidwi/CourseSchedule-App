package com.dicoding.courseschedule.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    private lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        viewModel.saved.observe(this, {
            if (it.getContentIfNotHandled() == true)
                onBackPressed()
            else {
                val message = getString(R.string.input_empty_message)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_insert -> {
                val factory = AddCourseViewModelFactory.createFactory(this)
                val viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

                val courseName = findViewById<EditText>(R.id.et_course_name).text.toString()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                val startTime = findViewById<TextView>(R.id.tv_start_clock).text.toString()
                val endTime = findViewById<TextView>(R.id.tv_end_clock).text.toString()
                val lecturerName = findViewById<TextView>(R.id.ed_lecturer).text.toString()
                val note = findViewById<EditText>(R.id.ed_note).text.toString()

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturerName, note)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        if (view.id == R.id.ib_start_time) {
            dialogFragment.show(supportFragmentManager, "timePickerStart")
            this.view = view
        }
        else if (view.id == R.id.ib_end_time) {
            dialogFragment.show(supportFragmentManager, "timePickerEnd")
            this.view = view
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (view.id) {
            R.id.ib_start_time -> {
                findViewById<TextView>(R.id.tv_start_clock).text = timeFormat.format(calender.time)
            }
            R.id.ib_end_time -> {
                findViewById<TextView>(R.id.tv_end_clock).text = timeFormat.format(calender.time)

            }
        }
    }

}