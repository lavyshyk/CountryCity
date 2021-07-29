package com.lavyshyk.countrycity.ui.countryListFragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mpv.BaseMpvFragment
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import io.reactivex.rxjava3.disposables.CompositeDisposable

/*
binding fragment by inflater
 */
class ListFragment : BaseMpvFragment<ICountryListView, CountryListPresenter>(),
    ICountryListView {

    private lateinit var mListCountry: MutableList<CountryDto>
    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mProgress: FrameLayout
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mStateRecycler: Parcelable


    private var bundle = Bundle()


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
        getPresenter().attachView(this)

        mAdapter = CountryAdapter()

        mAdapter.setItemClick { item ->
            bundle.putString(COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(
                R.id.action_listFragment_to_countryDetailsFragment,
                bundle
            )
            sharedPref.edit().putString(COUNTRY_NAME_FOR_NAV_KEY, item.name).apply()
        }



        binding.recView.adapter = mAdapter

        mProgress = binding.mPBarList

        binding.recView.layoutManager = LinearLayoutManager(this.activity)

        getPresenter().getCountryDataFromDataBase()
        getPresenter().getCountyDataFromAPI()

    }


    override fun onDestroyView() {

        fragmentListBinding = null
        getPresenter().onDestroyView()

        super.onDestroyView()
    }


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
        R.id.goToMap -> {
            findNavController().navigate(R.id.action_listFragment_to_mapCountiesFragment)
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

    private fun getSortStatus(): Boolean {
        Log.d("STS", "load status")
        return sharedPref.getBoolean(ITEM_SORT_STATUS, false)
    }

    override fun createPresenter() {
        mPresenter = CountryListPresenter()
    }

    override fun getPresenter(): CountryListPresenter = mPresenter

    override fun showCountryData(countries: MutableList<CountryDto>) {


        countries.let {


            if (getSortStatus()) mAdapter.repopulateSorted(it)
            else mAdapter.repopulateDescendingSorted(
                it
            )
        }
    }

    override fun showError(error: String, throwable: Throwable) {
        Snackbar.make(
            binding.root,
            getString(R.string.wrong_county), Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showProgress() {

        mProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgress.visibility = View.GONE
    }

}







