package com.lavyshyk.countrycity.ui.countryListFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.CountryApp.Companion.database
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.dto.CountryDataDto
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
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.let {
            sharedPref = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        }


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
        mAdapter.setItemClick { item ->
            val bundle = Bundle()
            bundle.putString(COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(R.id.action_listFragment_to_countryDetailsFragment, bundle)
        }
        binding.recView.adapter = mAdapter
        database?.let {
            mAdapter.repopulate(
                (it.countryDao().getListCountry()).transformEntitiesToCountryDto()
            )
        }
        getResultRequest()

    }

    override fun onDestroyView() {
        fragmentListBinding = null
        super.onDestroyView()
    }


    private fun getResultRequest() = retrofitService.getCountryInfo().enqueue(object :
        Callback<MutableList<CountryDataDto>> {
        override fun onResponse(
            call: Call<MutableList<CountryDataDto>>,
            response: Response<MutableList<CountryDataDto>>
        ) {
            mListCountry = response.body()
            mListCountry?.let {
                if (getSortStatus()) mAdapter.repopulateSorted(it) else mAdapter.repopulateDescendingSorted(
                    it
                )
            }
            mListCountry?.let {
                database?.countryDao()?.saveListCountry(it.transformEntitiesToCountry())
            }
            //mListCountry?.map { it.languages?.forEach { database?.languageDao()?.setLanguage(it.transformDtoToLanguage()) } }

        }

        override fun onFailure(call: Call<MutableList<CountryDataDto>>, t: Throwable) {
            t.printStackTrace()
        }
    })

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_one_icon, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (getSortStatus()) {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_big)
                .isChecked = getSortStatus()
            mAdapter.sortAndReplaceItem()
        } else {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_small)
                .isChecked = getSortStatus()
            mAdapter.sortDescendingAndReplaceItem()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sortCountries -> {
            if (item.isChecked) {
                mAdapter.sortDescendingAndReplaceItem()
                item.isChecked = SORT_TO_BIG
                saveSortStatus(item.isChecked)
                item.setIcon(R.drawable.ic_action_list_sort_to_big)
            } else {
                mAdapter.sortAndReplaceItem()
                item.isChecked = SORT_TO_SMALL
                saveSortStatus(item.isChecked)
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

    companion object {
        const val APP_PREFERENCES: String = "my_preferences_file"
        const val SORT_TO_BIG: Boolean = false
        const val SORT_TO_SMALL: Boolean = true
        const val ITEM_SORT_STATUS: String = "status_item"

    }

    fun saveSortStatus(status: Boolean) {
        sharedPref.edit()
            .putBoolean(ITEM_SORT_STATUS, status)
            .apply()
        Log.d("STS", "save status")

    }

    fun getSortStatus(): Boolean {
        Log.d("STS", "load status")
        return sharedPref.getBoolean(ITEM_SORT_STATUS, false)
    }

}





