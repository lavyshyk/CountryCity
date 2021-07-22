package com.lavyshyk.countrycity.ui.countryListFragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.CountryApp.Companion.database
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import com.lavyshyk.countrycity.util.transformEntitiesToCountry
import com.lavyshyk.countrycity.util.transformEntitiesToCountryDto
import com.lavyshyk.countrycity.util.transformToCountryDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/*
binding fragment by inflater
 */
class ListFragment : Fragment() {

    private lateinit var mListCountry: MutableList<CountryDto>
    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mProcess: FrameLayout
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    //private var mPressedItem: String = ""
    private lateinit var bundle: Bundle

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
        bundle = Bundle()
        binding.recView.layoutManager = LinearLayoutManager(this.activity)

        mAdapter = CountryAdapter()

        mAdapter.setItemClick { item ->
            bundle.putString(COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(
                R.id.action_listFragment_to_countryDetailsFragment,
                bundle
            )
            sharedPref.edit().putString(COUNTRY_NAME_FOR_NAV_KEY, item.name).apply()
        }

        //don't work??????????   
//        if (mPressedItem != ""){
//            val nameArg = NavArgument.Builder().setDefaultValue(mPressedItem).build()
//            val navGraph = findNavController().navInflater.inflate(R.navigation.nav_graph)
//            navGraph.addArgument("countryName",nameArg)
//            findNavController().setGraph(navGraph)
//
//
//
//        }


        binding.recView.adapter = mAdapter


        database?.let {
            mAdapter.repopulate(
                (it.countryDao().getListCountry()).transformEntitiesToCountryDto()
            )
        }
        mProcess = binding.mPBarList
        mProcess.visibility = View.VISIBLE

        getResultRequest()
    }


    override fun onDestroyView() {
        fragmentListBinding = null
        mCompositeDisposable.clear()

        super.onDestroyView()
    }


    private fun getResultRequest() {
        var subscription = retrofitService.getCountriesInfo()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe({ response ->
            response?.transformToCountryDto()?.let { mListCountry = it }
            mListCountry.let {

                if (getSortStatus()) mAdapter.repopulateSorted(it)
                else mAdapter.repopulateDescendingSorted(
                    it
                )
            }
            database?.countryDao()?.saveListCountry(mListCountry.transformEntitiesToCountry())

            mProcess.visibility = View.GONE
        },
            { throwable ->
                throwable.printStackTrace()
                mProcess.visibility = View.GONE
            }
        )
        mCompositeDisposable.add(subscription)
    }


/*
response by callback
 */
//        private fun getResultRequest() = retrofitService.getCountriesInfo()
//        enqueue(object :
//        Callback<MutableList<CountryDataInfo>> {
//        override fun onResponse(
//            call: Call<MutableList<CountryDataInfo>>,
//            response: Response<MutableList<CountryDataInfo>>
//        ) {
//            response.body()?.transformToCountryDto()?.let { mListCountry = it }
//            mListCountry.let {
//
//                if (getSortStatus()) mAdapter.repopulateSorted(it)
//                else mAdapter.repopulateDescendingSorted(
//                    it
//                )
//            }

//            database?.countryDao()?.saveListCountry(mListCountry.transformEntitiesToCountry())
//
//            mProcess.visibility = View.GONE
//        }
//
//        override fun onFailure(call: Call<MutableList<CountryDataInfo>>, t: Throwable) {
//            t.printStackTrace()
//            mProcess.visibility = View.GONE
//        }
//    })


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_sort_and_search, menu)
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
        R.id.searchCountry -> {
            activity?.showDialogQuickSearch("Search country", R.string.no, { it ->
                val s = bundle.getString(COUNTRY_NAME_KEY_FOR_DIALOG, "").toString()
                bundle.putString(COUNTRY_NAME_KEY, s)
                 findNavController().navigate(
                    R.id.action_listFragment_to_countryDetailsFragment,
                    bundle
                )
                sharedPref.edit().putString(COUNTRY_NAME_FOR_NAV_KEY, s).apply()
            }, R.string.yes, null, bundle)
            true
        }
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
        else ->
            super.onOptionsItemSelected(item)
    }


    private fun saveSortStatus(status: Boolean) {
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





