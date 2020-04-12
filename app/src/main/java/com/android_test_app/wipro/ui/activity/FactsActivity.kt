package com.android_test_app.wipro.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android_test_app.wipro.R
import com.android_test_app.wipro.ui.adapter.FactsAdapter
import com.android_test_app.wipro.util.NetworkUtility
import com.android_test_app.wipro.viewmodel.FactsViewModel

class FactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)

        if (NetworkUtility.isNetworkAvailable(this@FactsActivity)!!) {
            // Use the 'by viewModels()' Kotlin property delegate
            // from the activity-ktx artifact
            val viewModel: FactsViewModel by viewModels()
            val recyclerView: RecyclerView = findViewById(R.id.factsRecyclerView)

            viewModel.getFactsLiveData().observe(this@FactsActivity,
                Observer { factsApiResponse ->

                    // Update UI
                    val actionBarTitle = factsApiResponse?.title
                    supportActionBar?.title = actionBarTitle

                    val adapter = FactsAdapter()
                    recyclerView.adapter = adapter
                    adapter.factsList = factsApiResponse?.rows

                })
        } else {
            Toast.makeText(
                this@FactsActivity,
                resources.getString(R.string.network_unavailable_error_msg),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
