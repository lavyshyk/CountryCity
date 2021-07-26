package com.lavyshyk.countrycity.ui.countryDetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.CameraUpdateFactory.newLatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.APP_PREFERENCES
import com.lavyshyk.countrycity.COUNTRY_NAME_FOR_NAV_KEY
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mpv.BaseMpvFragment
import com.lavyshyk.countrycity.databinding.FragmentCountryDetailsBinding
import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import com.lavyshyk.countrycity.util.getDescription
import com.lavyshyk.countrycity.util.loadSvgFlag

class CountryDetailsFragment : BaseMpvFragment<ICountryDetailsView, CountryDetailPresenter>(),
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
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mSnackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            sharedPref = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        }
        mCountryName = arguments?.getString(COUNTRY_NAME_KEY) ?: sharedPref.getString(
            COUNTRY_NAME_FOR_NAV_KEY, "Belarus"
        ).toString()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCountryDetailsBinding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        binding = fragmentCountryDetailsBinding as @NonNull FragmentCountryDetailsBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().attachView(this)

        binding.mRecyclerCountryDescription.layoutManager = LinearLayoutManager(activity)

        mLanguageAdapter = LanguageAdapter()
        binding.mRecyclerCountryDescription.adapter = mLanguageAdapter

        mProcess = binding.mPBar
        mSRCountryDetail = binding.srCountryDetails
        mMapView = binding.mMapCountry

        mMapView.onCreate(savedInstanceState)

//        mSRCountryDetail.setOnRefreshListener {
//            getRequestAboutCountry(mCountryName)
//        }
        mSRCountryDetail.setOnRefreshListener {
            getPresenter().getCountryByName(mCountryName, true)
        }

//        mProcess.visibility = View.VISIBLE
//
//        Snackbar.make(
//            binding.root,
//            getString(R.string.wrong_county), Snackbar.LENGTH_SHORT
//        ).also { mSnackbar = it }

        //getRequestAboutCountry(mCountryName)

        getPresenter().getCountryByName(mCountryName, false)


    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroyView() {
       fragmentCountryDetailsBinding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }


//    private fun getRequestAboutCountry(nameCountry: String) {
//
//
//        val sub = retrofitService.getInfoAboutCountry(nameCountry)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({ response ->
//                response.let { mCountryListInfo = it.transformToCountryDetailDto() }
//                mCountryListInfo.get(0).also { countryDataDetail = it }
//                binding.mTVCountryDescription.text = getDescription(countryDataDetail)
//                countryDataDetail.languages.let { mLanguageAdapter.repopulate(it) }
//                binding.mIVCountryFlag.showSvgFlag(countryDataDetail.flag)
//
//                countryDataDetail.latlng.let { mCurrentLatLng = LatLng(it[0], it[1]) }
//
//                getCurrentLocationOnMap(mCurrentLatLng)
//                mProcess.visibility = View.GONE
//                mSRCountryDetail.isRefreshing = false
//            },
//                { t ->
//                    t.printStackTrace()
//                    mProcess.visibility = View.GONE
//                    mSRCountryDetail.isRefreshing = false
//                    mSnackbar.show()
//                    //return to fragment_list in backStack
//                    findNavController().navigate(R.id.action_countryDetailsFragment_to_listFragment)
//                }
//            )
//        mCompositeDisposable.add(sub)
//    }

//  response by callback
//    private fun getRequestAboutCountry(nameCountry: String) =
//        retrofitService.getInfoAboutCountry(nameCountry).enqueue(
//            object : Callback<MutableList<CountryDataDetail>> {
//                override fun onResponse(
//                    call: Call<MutableList<CountryDataDetail>>,
//                    response: Response<MutableList<CountryDataDetail>>
//                ) {
//                    response.body()?.let { mCountryListInfo = it.transformToCountryDetailDto() }
//                    mCountryListInfo.get(0).also { countryDataDetail = it }
//                    binding.mTVCountryDescription.text = getDescription(countryDataDetail)
//                    countryDataDetail.languages.let { mLanguageAdapter.repopulate(it) }
//                    binding.mIVCountryFlag.showSvgFlag(countryDataDetail.flag)
//
//                    countryDataDetail.latlng.let { mCurrentLatLng = LatLng(it[0],it[1]) }
//
//                    getCurrentLocationOnMap(mCurrentLatLng)
//                    mProcess.visibility = View.GONE
//                    mSRCountryDetail.isRefreshing = false
//                }
//
//                override fun onFailure(call: Call<MutableList<CountryDataDetail>>, t: Throwable) {
//                    t.printStackTrace()
//                    mProcess.visibility = View.GONE
//                    mSRCountryDetail.isRefreshing = false
//
//                }
//            }
//        )


//    fun AppCompatImageView.showSvgFlag(myUrl: String?) {
//        myUrl?.let {
//            if (it.lowercase(Locale.ENGLISH).endsWith("svg")) {
//                val imageLoader = ImageLoader.Builder(this.context)
//                    .componentRegistry {
//                        add(SvgDecoder(this@showSvgFlag.context))
//                    }
//                    .build()
//                val request = LoadRequest.Builder(this.context)
//                    .data(it)
//                    .target(this)
//                    .build()
//                imageLoader.execute(request)
//            } else {
//                this.load(myUrl)
//            }
//        }
//    }

    private fun getCurrentLocationOnMap(latLng: LatLng) {
        mMapView.getMapAsync {
            mGoogleMap = it
            mGoogleMap.moveCamera(newLatLng(latLng))
            mGoogleMap.addMarker(
                MarkerOptions().position(latLng)
            )
        }
    }


    override fun createPresenter() {
        mPresenter = CountryDetailPresenter()
    }

    override fun getPresenter(): CountryDetailPresenter = mPresenter

    override fun onMapReady(map: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun showCountryDetail(country: CountryDataDetailDto) {
        binding.srCountryDetails.isRefreshing = false
        binding.mTvCountryName.text = country.name
        binding.mTVCountryDescription.text = activity?.applicationContext?.let {
            getDescription(
                country,
                it
            )
        }
        country.languages.let { mLanguageAdapter.repopulate(it) }
        showSvgFlag(country.flag)

        country.latlng.let { mCurrentLatLng = LatLng(it[0], it[1]) }

        getCurrentLocationOnMap(mCurrentLatLng)


    }

    override fun showSvgFlag(url: String) {
        binding.mIVCountryFlag.loadSvgFlag(url)
    }

    override fun showError(error: String, throwable: Throwable) {
        Snackbar.make(
            binding.root,
            getString(R.string.wrong_county), Snackbar.LENGTH_SHORT
        ).also { mSnackbar = it }.show()
        //???
        findNavController().navigate(R.id.action_countryDetailsFragment_to_listFragment)
    }

    override fun showProgress() {
        mProcess.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProcess.visibility = View.GONE
    }
}




