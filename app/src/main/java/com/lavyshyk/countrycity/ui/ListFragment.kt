package com.lavyshyk.countrycity.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lavyshyk.countrycity.CountryApp.Companion.database
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.data.CountryData
import com.lavyshyk.countrycity.data.CountryDataDto
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.room.CountryDao
import com.lavyshyk.countrycity.util.transformEntitiesToCountry
import com.lavyshyk.countrycity.util.transformEntitiesToCountryDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
binding fragment by inflater
 */
class ListFragment : Fragment() {

    var mListCountry: MutableList<CountryDataDto>? = null
    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var countryDao: CountryDao
    private lateinit var list2: MutableList<CountryData>


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
        mAdapter = CountryAdapter()
        binding.recView.adapter = mAdapter
        database?.let { mAdapter.addItem((it.countryDao().getListCountry()).transformEntitiesToCountryDto()) }

        // database?.let { it.countryDao().getListCountry().also { list2 = it. } }
        //sharedPref
        getResultRequest()

//        countryDao.setListCountry(responseBody)


    }

    override fun onDestroyView() {

//        countryDao.setListCountry(convertCountry(mListCountry!!))

        fragmentListBinding = null
        super.onDestroyView()
    }


    private fun getResultRequest() = retrofitService.getCountryInfo().enqueue(object :
        Callback<MutableList<CountryDataDto>> {
        override fun onResponse(
            call: Call<MutableList<CountryDataDto>>,
            response: Response<MutableList<CountryDataDto>>
        ) {
//            mListCountry = response.body() as MutableList<CountryDataDto>
//            mListCountry?.let { mAdapter.addItem(mListCountry) }
            mListCountry = response.body()
            mListCountry?.let { mAdapter.addItem(it) }
            mListCountry?.let { database?.countryDao()?.setListCountry(it.transformEntitiesToCountry()) }


        }

        override fun onFailure(call: Call<MutableList<CountryDataDto>>, t: Throwable) {
            t.printStackTrace()
        }
    })


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_one_icon, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sortCountries -> {
            if (item.isChecked){
                mAdapter.sortDescendingAndReplaceItem()
                item.isChecked = false
                item.setIcon(R.drawable.ic_action_list_sort_to_big)
            }else{
                mAdapter.sortAndReplaceItem()
                item.isChecked = true
                item.setIcon(R.drawable.ic_action_list_sort_to_small)
            }
            true
        }

//        R.id.sortCountryFromBigToSmall -> {
////            mListCountry?.sortByDescending { it.area }
////            mListCountry?.let { mAdapter.addItem(it) }
//
//            true
//        }
//
//        R.id.sortCountryFromSmallToBig -> {
////            mListCountry?.sortBy { it.area }
////            mListCountry?.let { mAdapter.addItem(it) }
//            mAdapter.sortAndReplaceItem()
//            true
//        }

        else ->
            super.onOptionsItemSelected(item)
    }

}



