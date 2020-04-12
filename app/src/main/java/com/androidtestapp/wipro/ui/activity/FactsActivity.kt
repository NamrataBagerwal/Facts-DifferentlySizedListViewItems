package com.androidtestapp.wipro.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.androidtestapp.wipro.R
import com.androidtestapp.wipro.viewmodel.FactsViewModel

class FactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)


        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val viewModel: FactsViewModel by viewModels()

        viewModel.getFactsLiveData().observe(this@FactsActivity,
            Observer {
                factsApiResponse ->
                    // Update UI
            })
    }
}
