package com.lavyshyk.countrycity.ui.countryDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import coil.request.LoadRequest
import com.google.android.gms.maps.CameraUpdateFactory.newLatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.ERROR
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.databinding.FragmentCountryDetailsBinding
import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CountryDetailsFragment : Fragment() {

    private var mCountryName: String = "Belarus"
    private lateinit var mSRCountryDetail: SwipeRefreshLayout
    private lateinit var mProcess: FrameLayout
    private var mCountryListInfo: MutableList<CountryDataDetailDto>? = null
    private var fragmentCountryDetailsBinding: FragmentCountryDetailsBinding? = null
    private lateinit var binding: FragmentCountryDetailsBinding
    private lateinit var mLanguageAdapter: LanguageAdapter
    private lateinit var countryDataDetailDto: CountryDataDetailDto
    private lateinit var mMapView: MapView
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mCurrentLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(COUNTRY_NAME_KEY) ?: ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mTvCountryName.text = mCountryName

        binding.mRecyclerCountryDescription.layoutManager = LinearLayoutManager(activity)
        mLanguageAdapter = LanguageAdapter()
        binding.mRecyclerCountryDescription.adapter = mLanguageAdapter
        mProcess = binding.mPBar
        mSRCountryDetail = binding.srCountryDetails
        mMapView = binding.mMapCountry

        mMapView.onCreate(savedInstanceState)

        mSRCountryDetail.setOnRefreshListener {
            getRequestAboutCountry(mCountryName)
        }

        mProcess.visibility = View.VISIBLE

        getRequestAboutCountry(mCountryName)


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


    private fun getRequestAboutCountry(nameCountry: String) =
        retrofitService.getInfoAboutCountry(nameCountry).enqueue(
            object : Callback<MutableList<CountryDataDetailDto>> {
                override fun onResponse(
                    call: Call<MutableList<CountryDataDetailDto>>,
                    response: Response<MutableList<CountryDataDetailDto>>
                ) {
                    mCountryListInfo = response.body()
                    mCountryListInfo?.get(0)?.also { countryDataDetailDto = it }
                    binding.mTVCountryDescription.text = getDescription(countryDataDetailDto)
                    countryDataDetailDto.languages.let { mLanguageAdapter.repopulate(it) }
                    binding.mIVCountryFlag.loadSvgFlag(countryDataDetailDto.flag)

                    countryDataDetailDto.latlng?.let { mCurrentLatLng = LatLng(it[0],it[1]) }

                    getCurrentLocatonOnMap(mCurrentLatLng)
                    mProcess.visibility = View.GONE
                    mSRCountryDetail.isRefreshing = false
                }

                override fun onFailure(call: Call<MutableList<CountryDataDetailDto>>, t: Throwable) {
                    t.printStackTrace()
                    mProcess.visibility = View.GONE
                    mSRCountryDetail.isRefreshing = false

                }
            }
        )

    private fun getDescription(countryDataDetailDto: CountryDataDetailDto): String =
        "${countryDataDetailDto.name}. \n ${
            resources.getString(
                R.string.capital_is,
                countryDataDetailDto.capital
            )
        }. \n ${
            resources.getString(
                R.string.description_of_country,
                countryDataDetailDto.area.toString(),
                countryDataDetailDto.population.toString()
            )
        } \n${resources.getString(R.string.region, countryDataDetailDto.name, countryDataDetailDto.region)}"


    fun AppCompatImageView.loadSvgFlag(myUrl: String?) {
        myUrl?.let {
            if (it.lowercase(Locale.ENGLISH).endsWith("svg")) {
                val imageLoader = ImageLoader.Builder(this.context)
                    .componentRegistry {
                        add(SvgDecoder(this@loadSvgFlag.context))
                    }
                    .build()
                val request = LoadRequest.Builder(this.context)
                    .data(it)
                    .target(this)
                    .build()
                imageLoader.execute(request)
            } else {
                this.load(myUrl)
            }
        }
    }
    fun  getCurrentLocatonOnMap(latLng: LatLng){
        mMapView.getMapAsync { mGoogleMap = it
            mGoogleMap.moveCamera(newLatLng( latLng))
        }
    }
}




