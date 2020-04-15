package com.android_test_app.wipro.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android_test_app.wipro.R
import com.android_test_app.wipro.R.drawable.recyclerview_divider
import com.android_test_app.wipro.ui.adapter.FactsAdapter
import com.android_test_app.wipro.util.NetworkUtility
import com.android_test_app.wipro.viewmodel.FactsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FactsActivity : AppCompatActivity() {

    private val viewModel: FactsViewModel by viewModel()
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.factsRecyclerView) }
    private val swipeToRefresh: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh) }
    private val errorTextView: TextView by lazy { findViewById<TextView>(R.id.errorTextView) }
    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById<ContentLoadingProgressBar>(
            R.id.contentLoadingProgressBar
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)

        if (savedInstanceState == null) {
            showProgressBar()
        }

        observeFactsLiveData()

        setRecyclerViewDividerItemDecoration()

        setUpSwipeToRefresh()

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        progressBar.show()
    }

    private fun setRecyclerViewDividerItemDecoration() {
        val itemDecoration =
            DividerItemDecoration(this@FactsActivity, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this@FactsActivity, recyclerview_divider)?.let {
            itemDecoration.setDrawable(
                it
            )
        }
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun setUpSwipeToRefresh() {
//        swipeToRefresh.isRefreshing = true
        // Set the colors of the Pull To Refresh View
        swipeToRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        swipeToRefresh.setColorSchemeColors(Color.WHITE)

        swipeToRefresh.setOnRefreshListener {
            observeFactsLiveData()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun observeFactsLiveData() {
        viewModel.getFactsLiveData().observe(this@FactsActivity,
            Observer { factsApiResponse ->

                if (factsApiResponse?.rows.isNullOrEmpty()) {
                    if (NetworkUtility.isNetworkAvailable(this@FactsActivity) == null) {
                        showErrorView(getString(R.string.network_unavailable_error_msg))
                    } else {
                        showErrorView(getString(R.string.server_unavailable_error_msg))
                    }
                    viewModel.updateFactsLiveData()
                } else {
                    // Update UI
                    val actionBarTitle = factsApiResponse?.title
                    supportActionBar?.title = actionBarTitle

                    showFactsView()
                    val adapter = FactsAdapter()
                    recyclerView.adapter = adapter
                    adapter.factsList = factsApiResponse?.rows
                }
            })
    }

    private fun showFactsView() {
        hideProgressBar()
        errorTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showErrorView(errorMessage: String) {
        hideProgressBar()
        errorTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.text = errorMessage
    }


    private fun hideProgressBar() {
        progressBar.hide()
        progressBar.visibility = View.GONE

    }

}
