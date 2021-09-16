package com.lavyshyk.countrycity.ui.startFragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mvi.BaseMVIFragment
import com.lavyshyk.countrycity.databinding.FragmentStartBinding
import com.lavyshyk.countrycity.service.LocationTrackingService
import com.lavyshyk.countrycity.util.checkLocationPermission
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.util.*

/*
binding fragment by constructor
 */
class StartFragment :
    BaseMVIFragment<StartIntent, StartAction, StartState>(R.layout.fragment_start) {

    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private var fragmentStartBinding: FragmentStartBinding? = null
    private lateinit var mGeocoder: Geocoder
    private var mAddresses: List<Address> = emptyList()
    private lateinit var mProcess: FrameLayout
    private lateinit var mRecycler: RecyclerView
    private lateinit var mAdapter: NewsAdapter
    private val mViewModel: StartViewModel by stateViewModel()
    private val intentFilter = IntentFilter()
        .apply { addAction(LocationTrackingService.NEW_LOCATION_ACTION) }
    private var mLocationTemp = Location(LocationManager.GPS_PROVIDER)
        .apply {
            this.latitude = 0.0
            this.longitude = 0.0
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
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
        mViewModel.state.observe(viewLifecycleOwner, {
            viewState = it
            render(it)
        })

        initData()
        initEvent()
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


    }

    override fun initData() {
        getGetNewsFromLocation()
    }

    private fun getGetNewsFromLocation() {
        mGeocoder = Geocoder(context, Locale.getDefault())
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    when (intent.action) {
                        LocationTrackingService.NEW_LOCATION_ACTION -> {
                            intent.getParcelableExtra<Location>("location")
                                ?.let { mLocationTemp = it }
                            var flag = false
                            try {
                                if (mAddresses.isEmpty()) {
                                    mAddresses = mGeocoder.getFromLocation(
                                        mLocationTemp.latitude,
                                        mLocationTemp.longitude,
                                        1
                                    )
                                    flag = true
                                }
                            } catch (e: Exception) {
                                dispatchIntent(StartIntent.Exception(e))
                            }
                            if (!mAddresses.isNullOrEmpty() && flag) {
                                mAddresses?.let { dispatchIntent(StartIntent.LoadNews(it.last().countryCode)) }
                            }
                        }
                    }
                }
            }
        }
        context?.registerReceiver(mBroadcastReceiver, intentFilter)
    }

    override fun initEvent() {
    }

    override fun render(state: StartState) {

        when (state) {
            is StartState.Loading -> {
                if (state.loading) {
                    mProcess.visibility = View.VISIBLE
                } else {
                    mProcess.visibility = View.GONE
                }
            }
            is StartState.ResultNews -> {
                mAdapter.repopulate(state.data.toMutableList())
            }
            is StartState.Exception -> {
                showError(state.t.message.toString(), state.t)
            }

        }
    }

    private fun dispatchIntent(intent: StartIntent) {
        mViewModel.dispatchIntent(intent)
    }

    override fun onDestroyView() {
        context?.unregisterReceiver(mBroadcastReceiver)
        fragmentStartBinding = null
        super.onDestroyView()
    }

    override fun showError(error: String, throwable: Throwable) {
        fragmentStartBinding?.let { Snackbar.make(it.root, error, Snackbar.LENGTH_SHORT).show() }
    }
}




