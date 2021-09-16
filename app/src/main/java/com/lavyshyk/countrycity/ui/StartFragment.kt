package com.lavyshyk.countrycity.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.databinding.FragmentStartBinding
import com.lavyshyk.countrycity.service.LocationTrackingService
import com.lavyshyk.countrycity.ui.ext.showAlertDialog
import com.lavyshyk.countrycity.util.checkLocationPermission

/*
binding fragment by constructor
 */
class StartFragment : Fragment(R.layout.fragment_start) {
    private var fragmentStartBinding: FragmentStartBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context?.checkLocationPermission() == true){
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



        val binding = FragmentStartBinding.bind(view)
        fragmentStartBinding = binding
        binding.tViewStart.text = resources.getString(R.string.hello_blank_fragment)
        activity?.showAlertDialog()
    }

    override fun onDestroyView() {
        fragmentStartBinding = null
        super.onDestroyView()
    }
}