package com.lavyshyk.countrycity.ui.mapCountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mvp.BaseMvpKoinFragment
import com.lavyshyk.domain.dto.CountryInfoMapDto
import org.koin.android.ext.android.inject

class MapCountryFragment : BaseMvpKoinFragment<IMapCountryView, MapCountryPresenter>(),
    OnMapReadyCallback, IMapCountryView {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mSnackbar: Snackbar
    private lateinit var mProgress: FrameLayout
    private val mMapCountryPresenter: MapCountryPresenter by inject()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mCountryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(COUNTRY_NAME_KEY) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_of_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapCountryPresenter.attachView(this)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mProgress = view.findViewById(R.id.mPBarMap)
        mapFragment.getMapAsync(this)
        mMapCountryPresenter.getAllCountryData()
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
    }

    override fun showCountryOnMap(countries: MutableList<CountryInfoMapDto>) {
        countries.forEach {
            mGoogleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latlng[0], it.latlng[1]))
                    .title(it.name)
                    .apply {
                        if (mCountryName == it.name.lowercase() ) {
                            this.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            this.snippet("${it.capital}, ${it.latlng}")
                        }
                    }


            )
        }
    }

    override fun showError(error: String, throwable: Throwable) {
        view?.let {
            Snackbar.make(
                it.rootView,
                error, Snackbar.LENGTH_SHORT
            ).also { mSnackbar = it }.show()
        }
        throwable.printStackTrace()
        findNavController().navigateUp()
    }

    override fun showProgress() {
        mProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgress.visibility = View.GONE
    }
}
