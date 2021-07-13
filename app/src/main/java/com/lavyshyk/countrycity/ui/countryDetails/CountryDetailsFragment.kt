package com.lavyshyk.countrycity.ui.countryDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import coil.request.LoadRequest
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.ERROR
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.databinding.FragmentCountryDetailsBinding
import com.lavyshyk.countrycity.dto.CountryDataDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CountryDetailsFragment : Fragment() {

    private lateinit var mCountryName: String
    private var mCountryListInfo: MutableList<CountryDataDto>? = null
    private var fragmentCountryDetailsBinding: FragmentCountryDetailsBinding? = null
    private lateinit var binding: FragmentCountryDetailsBinding
    private lateinit var mLanguageAdapter: LanguageAdapter
    private lateinit var countryDataDto: CountryDataDto

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
        binding.mRecyclerCountryDescription.layoutManager = LinearLayoutManager(activity)
        mLanguageAdapter = LanguageAdapter()
        binding.mRecyclerCountryDescription.adapter = mLanguageAdapter
        binding.mTvCountryName.text = mCountryName





        getRequestAboutCountry(mCountryName)
       // Uri.parse("https://restcountries.eu/data/umi.svg"


        //binding.mIVCountryFlag = getImage()
        //binding.mTVCountryDescription.text = mCountryListInfo?.get(0)?.toString()

        //binding.mIVCountryFlag = setMap()
    }

    override fun onDestroyOptionsMenu() {
        fragmentCountryDetailsBinding = null
        super.onDestroyOptionsMenu()
    }

    private fun getRequestAboutCountry(nameCountry: String) =
        retrofitService.getInfoAboutCountry(nameCountry).enqueue(
            object : Callback<MutableList<CountryDataDto>> {
                override fun onResponse(
                    call: Call<MutableList<CountryDataDto>>,
                    response: Response<MutableList<CountryDataDto>>
                ) {
                    mCountryListInfo = response.body()
                    countryDataDto = mCountryListInfo?.get(0)!!
                    binding.mTVCountryDescription.text = getDescription(countryDataDto)
                    mLanguageAdapter.addList(countryDataDto.languages!!)
                    binding.mIVCountryFlag.loadSvgOrOthers(countryDataDto.flag)
                }

                override fun onFailure(call: Call<MutableList<CountryDataDto>>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )

    private fun getDescription(countryDataDto: CountryDataDto): String =
        "${countryDataDto.name}. \n ${
            resources.getString(
                R.string.capital_is,
                countryDataDto.capital
            )
        }. \n ${
            resources.getString(
                R.string.description_of_country,
                countryDataDto.area.toString(),
                countryDataDto.population.toString()
            )
        } \n${resources.getString(R.string.region, countryDataDto.name, countryDataDto.region)}"

    fun AppCompatImageView.loadSvgOrOthers(myUrl: String?) {
        myUrl?.let {
            if (it.lowercase(Locale.ENGLISH).endsWith("svg")) {
                val imageLoader = ImageLoader.Builder(this.context)
                    .componentRegistry {
                        add(SvgDecoder(this@loadSvgOrOthers.context))
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
}




