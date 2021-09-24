package com.lavyshyk.countrycity.ui.startFragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvi.BaseMVIFragment
import com.lavyshyk.countrycity.databinding.FragmentStartBinding
import com.lavyshyk.countrycity.service.LocationTrackingService
import com.lavyshyk.countrycity.ui.ext.showAlertDialog
import com.lavyshyk.countrycity.ui.sreensaver.RationaleFragment
import com.lavyshyk.countrycity.util.checkLocationPermission
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class StartFragment :
    BaseMVIFragment<StartIntent, StartAction, StartState, StartViewModel>(StartViewModel::class.java) {

    private var fragmentStartBinding: FragmentStartBinding? = null
    private lateinit var mProcess: FrameLayout
    private lateinit var mRecycler: RecyclerView
    private lateinit var mAdapter: NewsAdapter
    private lateinit var mSRefresh: SwipeRefreshLayout

    private val mLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    startLocationService()
                }
                else -> {
                    dispatchIntent(StartIntent.LoadNews)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showRationaleDialog()
        } else {
            mLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        setFragmentResultListener(RATIONALE_KEY) { _, bundle ->
            val allowAfterRationale = bundle.getBoolean(RESULT_KEY)
            if (allowAfterRationale) {
                mLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun initUI(view: View) {
        fragmentStartBinding = FragmentStartBinding.bind(view)
        val binding = fragmentStartBinding
        binding?.tViewStart?.text = resources.getString(R.string.hello_blank_fragment)
        binding?.mProgressBar?.let { mProcess = it }
        binding?.mRvListNews?.let { mRecycler = it }
        binding?.mRvListNews?.layoutManager = LinearLayoutManager(this.activity)
        mAdapter = NewsAdapter()
        mRecycler.adapter = mAdapter
        binding?.srNews?.let { mSRefresh = it }
    }

    override fun render(state: StartState) {
        when (state) {
            is StartState.Loading -> {
                if (state.loading) {
                    mProcess.visibility = View.VISIBLE
                } else {
                    mProcess.visibility = View.GONE
                    mSRefresh.isRefreshing = false
                }
            }
            is StartState.ResultNews -> {
                if (state.data.isNotEmpty()) {
                    mAdapter.repopulate(state.data.toMutableList())
                } else {
                    activity?.showAlertDialog()
                    dispatchIntent(StartIntent.LoadNews)
                }
            }
            is StartState.Exception -> {
                showError(state.t.message.toString(), state.t)
            }
        }
    }

    override fun initData() {
        dispatchIntent(StartIntent.LoadNewsByCode)
    }

    override fun initEvent() {
        mSRefresh.setOnRefreshListener {
            initData()
        }

        mAdapter.setItemClick { key, item ->
            when (key) {
                NEWS -> {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(item.url)
                    startActivity(openURL)
                }
            }
        }
    }

    private fun startLocationService() {
        if (context?.checkLocationPermission() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.context?.startForegroundService(
                    Intent(
                        this.context,
                        LocationTrackingService::class.java
                    )
                )
            } else {
                this.context?.startService(
                    Intent(
                        this.context,
                        LocationTrackingService::class.java
                    )
                )
            }
        }
    }

    private fun showRationaleDialog() {
        RationaleFragment().show(parentFragmentManager, TAG_RATION)
    }

    override fun showError(error: String, throwable: Throwable) {
        fragmentStartBinding?.let { Snackbar.make(it.root, error, Snackbar.LENGTH_SHORT).show() }
    }

    override fun onDestroyView() {
        fragmentStartBinding = null
        super.onDestroyView()
    }
}



