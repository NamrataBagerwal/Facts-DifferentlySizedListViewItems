package com.android_test_app.wipro.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android_test_app.wipro.R
import com.android_test_app.wipro.ui.adapter.FactsAdapter
import com.android_test_app.wipro.util.NetworkUtility
import com.android_test_app.wipro.viewmodel.FactsViewModel

class FactsActivity : AppCompatActivity() {

    private val viewModel: FactsViewModel by viewModels()
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.factsRecyclerView) }
      private  val swipeToRefresh: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)

        setUpSwipeToRefresh()

        if (NetworkUtility.isNetworkAvailable(this@FactsActivity)!!) {
            observeFactsLiveData()
        } else {
            showToast(resources.getString(R.string.network_unavailable_error_msg))
        }
    }

    private fun setUpSwipeToRefresh() {
        // Set the colors of the Pull To Refresh View
        swipeToRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        swipeToRefresh.setColorSchemeColors(Color.WHITE)

        swipeToRefresh.setOnRefreshListener {
            viewModel.updateFactsLiveData()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this@FactsActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun observeFactsLiveData() {
        viewModel.getFactsLiveData().observe(this@FactsActivity,
            Observer { factsApiResponse ->
                // Update UI
                val actionBarTitle = factsApiResponse?.title
                supportActionBar?.title = actionBarTitle

               if(factsApiResponse?.rows.isNullOrEmpty())
                   showToast(getString(R.string.server_unavailable_error_msg))
                else{
                   val adapter = FactsAdapter()
                   recyclerView.adapter = adapter
                   adapter.factsList = factsApiResponse?.rows
               }


            })
    }
}
