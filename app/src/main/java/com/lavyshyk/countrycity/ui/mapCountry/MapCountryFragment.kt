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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mpv.BaseMpvFragment
import com.lavyshyk.countrycity.dto.CountryInfoMapDto
import org.koin.android.ext.android.inject

class MapCountryFragment: BaseMpvFragment<IMapCountryView, MapCountryPresenter>(),
    OnMapReadyCallback, IMapCountryView {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mSnackbar: Snackbar
    private lateinit var mProgress: FrameLayout
    private val mMapCountryPresenter: MapCountryPresenter by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_of_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getPresenter().attachView(this)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mProgress = view.findViewById(R.id.mPBarMap)

        mapFragment?.getMapAsync(this)
        //getPresenter().getAllCountryData()
        mMapCountryPresenter.getAllCountryData()
        mMapCountryPresenter.attachView(this)


    }


    override fun onMapReady(map: GoogleMap) {

        mGoogleMap = map

    }

//    override fun createPresenter() {
//        mPresenter = MapCountryPresenter()
//    }
//
//    override fun getPresenter(): MapCountryPresenter = mPresenter

    override fun showCountryOnMap(countries: MutableList<CountryInfoMapDto>) {



        countries.forEach {

                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(
                            com.google.android.gms.maps.model.LatLng(
                                it.latlng[0],
                                it.latlng[1]
                            )
                        )
                        .title(it.name)
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

        findNavController().navigate(R.id.action_mapCountiesFragment_to_listFragment)
    }

    override fun showProgress() {

        mProgress.visibility =View.VISIBLE

    }

    override fun hideProgress() {

        mProgress.visibility = View.GONE

    }


}
