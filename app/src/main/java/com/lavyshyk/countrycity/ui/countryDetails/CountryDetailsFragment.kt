package com.lavyshyk.countrycity.ui.countryDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.COUNTRY_NAME_FOR_NAV_KEY
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY_FOR_DIALOG
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mvp.BaseMvpKoinFragment
import com.lavyshyk.countrycity.databinding.FragmentCountryDetailsBinding
import com.lavyshyk.countrycity.ui.ext.getDescription
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import com.lavyshyk.countrycity.util.loadSvgFlag
import com.lavyshyk.domain.dto.CountryDataDetailDto
import org.koin.android.ext.android.inject

class CountryDetailsFragment : BaseMvpKoinFragment<ICountryDetailsView, CountryDetailPresenter>(),
    OnMapReadyCallback, ICountryDetailsView {

    private lateinit var mCountryName: String
    private lateinit var mSRCountryDetail: SwipeRefreshLayout
    private lateinit var mProcess: FrameLayout
    private var fragmentCountryDetailsBinding: FragmentCountryDetailsBinding? = null
    private lateinit var binding: FragmentCountryDetailsBinding
    private lateinit var mLanguageAdapter: LanguageAdapter
    private lateinit var mMapView: MapView
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mCurrentLatLng: LatLng
    private var mAreaCounty: Float = 0.0F
    private var bundle = Bundle()
    private val mCountryDetailPresenter: CountryDetailPresenter by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName =
            arguments?.getString(COUNTRY_NAME_KEY) ?: mCountryDetailPresenter.getSharedPrefString(
                COUNTRY_NAME_FOR_NAV_KEY
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCountryDetailsBinding =
            FragmentCountryDetailsBinding.inflate(inflater, container, false)
        binding = fragmentCountryDetailsBinding as @NonNull FragmentCountryDetailsBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // getPresenter().attachView(this)
        // getPresenter().setArgumentFromView(mCountryName)
        mCountryDetailPresenter.setArgumentFromView(mCountryName)
        mCountryDetailPresenter.attachView(this)

        binding.mRecyclerCountryDescription.layoutManager = LinearLayoutManager(activity)

        mLanguageAdapter = LanguageAdapter()
        binding.mRecyclerCountryDescription.adapter = mLanguageAdapter

        mProcess = binding.mPBar
        mSRCountryDetail = binding.srCountryDetails
        mMapView = binding.mMapCountry


        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

        mSRCountryDetail.setOnRefreshListener {
            mCountryDetailPresenter.getCountryByName(true)
        }
        mCountryDetailPresenter.getCountryByName(false)
    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onDestroyView() {
        fragmentCountryDetailsBinding = null
        mCountryDetailPresenter.detachView()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
    private fun getCurrentLocationOnMap(latLng: LatLng, countryName: String, zoom: Float) {
        mGoogleMap.moveCamera(newLatLngZoom(latLng, zoom))
        mGoogleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(countryName)
        )
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
    }

    override fun showCountryDetail(country: CountryDataDetailDto,zoom: Float) {
        binding.srCountryDetails.isRefreshing = false
        binding.mTvCountryName.text = country.name
        mAreaCounty = country.area
        binding.mTVCountryDescription.text = activity?.applicationContext?.let {
            getDescription(country, it)
        }
        country.languages.let { mLanguageAdapter.repopulate(it) }
        showSvgFlag(country.flag)
        country.latlng.let { mCurrentLatLng = LatLng(it[0], it[1]) }
        getCurrentLocationOnMap(mCurrentLatLng, country.name, zoom)
    }

    override fun showSvgFlag(url: String) {
        binding.mIVCountryFlag.loadSvgFlag(url)
    }

    override fun showError(error: String, throwable: Throwable) {
        Snackbar.make(
            binding.root,
            error, Snackbar.LENGTH_INDEFINITE
        ).setAction(
            getString(R.string.try_again)
        ) {
            activity?.showDialogQuickSearch(
                "Search country",
                R.string.no,
                { findNavController().navigate(R.id.listFragment) },
                R.string.yes,
                {
                    val nameCountry = bundle.getString(COUNTRY_NAME_KEY_FOR_DIALOG, "").toString()
                    mCountryDetailPresenter.setArgumentFromView(nameCountry)
                    mCountryDetailPresenter.getCountryByName(false)
                    mCountryDetailPresenter.putSharedPrefString(COUNTRY_NAME_FOR_NAV_KEY, nameCountry)
                },
                bundle
            )
        }.show()
    }

    override fun showProgress() {
        mProcess.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProcess.visibility = View.GONE
    }

}




