package com.lavyshyk.countrycity.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.data.CountryData
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.room.Country
import com.lavyshyk.countrycity.room.CountryDao
import com.lavyshyk.countrycity.room.CountryDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
binding fragment by inflater
 */
class ListFragment : Fragment() {

    var responseBody: MutableList<CountryData>? = null
    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var db: CountryDatabase
    private lateinit var countryDao: CountryDao
    private lateinit var list2: MutableList<Country>
    //lateinit var call: Callback<MutableList<CountryData>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recView.layoutManager = LinearLayoutManager(this.activity)

//       }

        getResultRequest()


//        countryDao.setListCountry(responseBody)


    }

    override fun onDestroyView() {

        countryDao.setListCountry(convertCountry(responseBody!!))

        fragmentListBinding = null
        super.onDestroyView()
    }

    private fun getResultRequest() = retrofitService.getCountryInfo().enqueue(object :
        Callback<MutableList<CountryData>> {
        override fun onResponse(
            call: Call<MutableList<CountryData>>,
            response: Response<MutableList<CountryData>>
        ) {
            responseBody = response.body() as MutableList<CountryData>
            mAdapter = CountryAdapter(responseBody!!)
            binding.recView.adapter = mAdapter
        }

        override fun onFailure(call: Call<MutableList<CountryData>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sortCountryFromBigToSmall -> {
            responseBody?.sortByDescending { it.area }
            mAdapter.notifyDataSetChanged()
            true
        }

        R.id.sortCountryFromSmallToBig -> {
            responseBody?.sortBy { it.area }
            mAdapter.notifyDataSetChanged()
            true
        }

        else ->
            super.onOptionsItemSelected(item)
    }


    fun convertCountry(list: MutableList<CountryData>): MutableList<Country> {
        for (i in 0..list.size) {
            list2[i].name = list[i].name
            list2[i].capital = list[i].capital

        }
        return list2
    }
}



