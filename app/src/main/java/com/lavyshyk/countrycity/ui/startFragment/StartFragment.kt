package com.lavyshyk.countrycity.ui.startFragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvi.BaseMVIFragment
import com.lavyshyk.countrycity.databinding.FragmentStartBinding
import com.lavyshyk.countrycity.service.LocationTrackingService
import com.lavyshyk.countrycity.ui.ext.showAlertDialog
import com.lavyshyk.countrycity.util.checkLocationPermission
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class StartFragment :
    BaseMVIFragment<StartIntent, StartAction, StartState, StartViewModel>(StartViewModel::class.java) {

    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private var fragmentStartBinding: FragmentStartBinding? = null
    private var mAddresses: List<Address> = emptyList()
    private lateinit var mProcess: FrameLayout
    private lateinit var mRecycler: RecyclerView
    private lateinit var mAdapter: NewsAdapter
    private var mFlagGetLocation = false
    private lateinit var mGeocoder: Geocoder
    private val intentFilter = IntentFilter()
        .apply { addAction(LocationTrackingService.NEW_LOCATION_ACTION) }
    private var mLocationTemp = Location(LocationManager.GPS_PROVIDER)
        .apply {
            this.latitude = 0.0
            this.longitude = 0.0
        }
    private val mLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { granted ->
        when {
            granted -> {
                // user granted permission
                startLocationService()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // user denied permission and set Don't ask again.
                showSettingsDialog()
            }
            else -> {
                //showToast(R.string.denied_toast)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mGeocoder = Geocoder(context, Locale.getDefault())

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    when (intent.action) {
                        LocationTrackingService.NEW_LOCATION_ACTION -> {
                            intent.getParcelableExtra<Location>(LOCATION_KEY)
                                ?.let { mLocationTemp = it }
                            getGetNewsFromLocation(mLocationTemp)
                        }
                    }
                }
            }
        }
        context?.registerReceiver(mBroadcastReceiver, intentFilter)

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // we need to tell user why do we need permission
            showRationaleDialog()
        } else {
            mLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setFragmentResultListener(RATIONALE_KEY) { _, bundle ->
            val isWantToAllowAfterRationale = bundle.getBoolean(RESULT_KEY)
            if (isWantToAllowAfterRationale) {
                mLocationPermission.launch(Manifest.permission.CAMERA)
            }
        }

        setFragmentResultListener(SETTINGS_KEY) { _, bundle ->
            val isWantToOpenSettings = bundle.getBoolean(RESULT_KEY)
            if (isWantToOpenSettings) {
                openSettings()
            }
        }
        initUI(view)
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

    override fun initData() {}
    override fun initEvent() {}

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
                if (state.data.isNotEmpty()){
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

    override fun onDestroyView() {
        context?.unregisterReceiver(mBroadcastReceiver)
        fragmentStartBinding = null
        super.onDestroyView()
    }

    override fun showError(error: String, throwable: Throwable) {
        fragmentStartBinding?.let { Snackbar.make(it.root, error, Snackbar.LENGTH_SHORT).show() }
    }

    private fun getGetNewsFromLocation(location: Location){
        try {
            if (mAddresses.isEmpty()) {
                mFlagGetLocation = true

                mAddresses = mGeocoder
                    .getFromLocation(location.latitude, location.longitude, 1)
                if (mAddresses.isNotEmpty() && mFlagGetLocation)
                dispatchIntent(StartIntent.LoadNewsByCode(mAddresses.last().countryCode))
            }
        } catch (e: Exception) {
            dispatchIntent(StartIntent.Exception(e))
        }
    }
    private fun showToast(textId: Int) {
        Toast.makeText(context, textId, Toast.LENGTH_SHORT).show()
    }

    private fun showRationaleDialog() {
        RationaleFragment().show(parentFragmentManager, TAG_RATION)
    }

    private fun showSettingsDialog() {
        DontAskAgainFragment().show(parentFragmentManager, TAG_ASK)
    }
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", requireContext().packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
//    companion object {
//        const val RATIONALE_KEY = "rationale_tag"
//        const val SETTINGS_KEY = "settings_tag"
//        const val RESULT_KEY = "camera_result_key"
//    }
}




