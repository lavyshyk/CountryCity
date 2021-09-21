package com.lavyshyk.countrycity.ui.countryListFragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvvm.IBaseMvvmView
import com.lavyshyk.countrycity.databinding.BottomSheetFragmentBinding
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.countrycity.service.LocationTrackingService
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import com.lavyshyk.countrycity.util.convertToCorrectArea
import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.FilterRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class CountriesListFragment : ScopeFragment(), IBaseMvvmView {

    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var mProgress: FrameLayout
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var mSRCountryList: SwipeRefreshLayout
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var bottomSheetFragmentBinding: BottomSheetFragmentBinding? = null
    private var leftPopulation: Float = MIN_POPULATION
    private var rightPopulation: Float = MAX_POPULATION
    private var leftArea: Float = MIN_AREA
    private var rightArea: Float = MAX_AREA
    private val mViewModel: CountryListViewModel by stateViewModel()
    private val mFilterRepository: FilterRepository by inject()
    private val mSharedPreferences: SharedPrefRepository by inject()
    private lateinit var headerPeek: AppCompatImageView
    private var bundle = Bundle()
    private lateinit var mEditText: AppCompatEditText
    private lateinit var mRSliderArea: RangeSlider
    private lateinit var mRSliderPopulation: RangeSlider
    private lateinit var mTextMaxArea: AppCompatTextView
    private lateinit var mTextMinArea: AppCompatTextView
    private lateinit var mTextMaxPopulation: AppCompatTextView
    private lateinit var mTextMinPopulation: AppCompatTextView
    private val intentFilter =
        IntentFilter().apply { addAction(LocationTrackingService.NEW_LOCATION_ACTION) }
    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private var mLocationTemp = Location(LocationManager.GPS_PROVIDER).apply {
        this.latitude = 0.0
        this.longitude = 0.0
    }

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

        initUI(view)

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    when (intent.action) {
                        LocationTrackingService.NEW_LOCATION_ACTION -> {
                            intent.getParcelableExtra<Location>(LOCATION_KEY)
                                ?.let { mLocationTemp = it }
                        }
                    }
                }
            }
        }
        context?.registerReceiver(mBroadcastReceiver, intentFilter)

        mViewModel.putCurrentLocation(mLocationTemp)

        mSRCountryList.setOnRefreshListener { mViewModel.getCountriesFromApi(true) }

        mViewModel.getCountriesFromApi()

        mViewModel.mSortedCountryList.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                    if (it.loading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
                is Outcome.Next -> {
                    showCountryData(it.data)
                }
                is Outcome.Failure -> {
                    showError(it.t.message.toString(), it.t)
                }
                is Outcome.Success -> {
                    hideProgress()
                }
            }
        })

        mViewModel.mCountyLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                    if (it.loading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
                is Outcome.Next -> {
                    showCountryData(it.data)

                    mViewModel.saveDataFromApiToDB(it.data)
                }
                is Outcome.Failure -> {
                    mViewModel.getCountryListFromDB()
                    showError(it.t.message.toString(), it.t)
                }
                is Outcome.Success -> {
                    hideProgress()
                }
            }
        })
        mAdapter.setItemClick { name, item ->
            when (name) {
                item.nativeName -> {
                    bundle.putString(COUNTRY_NAME_KEY, item.name)
                    findNavController().navigate(
                        R.id.action_listFragment_to_countryDetailsFragment,
                        bundle )
                    mSharedPreferences.putStringSharedPref(COUNTRY_NAME_FOR_NAV_KEY, item.name)
                }
                DISLIKE -> mAdapter.refreshLikeItem(item, false)
                LIKE -> mAdapter.refreshLikeItem(item, true)
            }
        }

        headerPeek.setOnClickListener {
            if (sheetBehavior.state == STATE_EXPANDED) {
                sheetBehavior.state = STATE_COLLAPSED
                imm.hideSoftInputFromWindow(
                    headerPeek.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } else {
                sheetBehavior.state = STATE_EXPANDED
            }
        }

        sheetBehavior.addBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_EXPANDED -> {
                            setStartStateOfBottomSheet()
                        }
                        STATE_COLLAPSED -> {
                            if (!mEditText.text.isNullOrEmpty()) {
                                mFilterRepository.processNewDistance(
                                    mEditText.text.toString().toFloat()
                                )
                            }
                            mViewModel.getSortedListCountry()
                            sheetBehavior.peekHeight = PEEK_HEIGHT
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

        mRSliderArea.addOnChangeListener(
            RangeSlider.OnChangeListener { slider, value, fromUser ->
                leftArea = slider.values[0]
                rightArea = slider.values[1]
                mTextMinArea.text = String.format("%5.0f km2",leftArea)
                mTextMaxArea.text = String.format("%5.0f km2",rightArea)
                mFilterRepository.processNewArea(leftArea, rightArea)
            })
        mRSliderPopulation.addOnChangeListener(
            RangeSlider.OnChangeListener { slider, value, fromUser ->
                leftPopulation = slider.values[0]
                rightPopulation = slider.values[1]
                mTextMinPopulation.text = String.format("%5.0f ppl",leftPopulation)
                mTextMaxPopulation.text = String.format("%5.0f ppl",rightPopulation)
                mFilterRepository.processNewPopulation(leftPopulation, rightPopulation)
            })
    }

    override fun onDestroyView() {
        bottomSheetFragmentBinding = null
        fragmentListBinding = null
        super.onDestroyView()
        context?.unregisterReceiver(mBroadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_sort_and_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (mSharedPreferences.getBooleanFromSharedPref(ITEM_SORT_STATUS)) {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_big)
                .isChecked = mSharedPreferences.getBooleanFromSharedPref(ITEM_SORT_STATUS)
            mAdapter.sortAndReplaceItem()
        } else {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_small)
                .isChecked = mSharedPreferences.getBooleanFromSharedPref(ITEM_SORT_STATUS)
            mAdapter.sortDescendingAndReplaceItem()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.searchCountry -> {
            var search = ""
            activity?.showDialogQuickSearch(
                getString(R.string.search_country), R.string.no, null,
                R.string.yes, {
                    search = bundle.getString(COUNTRY_NAME_KEY_FOR_DIALOG, "").toString().lowercase()
                    mFilterRepository.processNewQuery(search)
                    mFilterRepository.processNewArea(MIN_AREA, MAX_AREA)
                    mFilterRepository.processNewDistance(Float.MAX_VALUE)
                    mFilterRepository.processNewPopulation(MIN_POPULATION, MAX_POPULATION)
                    mViewModel.getSortedListCountry()
                }, bundle
            )
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
                mSharedPreferences.putBooleanSharedPref(ITEM_SORT_STATUS, item.isChecked)
                item.setIcon(R.drawable.ic_action_list_sort_to_big)
            } else {
                mAdapter.sortAndReplaceItem()
                item.isChecked = SORT_TO_SMALL
                mSharedPreferences.putBooleanSharedPref(ITEM_SORT_STATUS, item.isChecked)
                item.setIcon(R.drawable.ic_action_list_sort_to_small)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showCountryData(countries: MutableList<CountryDto>) {
        countries.let { mAdapter.repopulate(it) }
    }

    override fun showError(error: String, throwable: Throwable) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        mProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgress.visibility = View.GONE
        mSRCountryList.isRefreshing = false
    }

    private fun initUI(view: View) {
        mProgress = binding.mPBarList
        mAdapter = CountryAdapter()
        binding.recView.adapter = mAdapter
        binding.recView.layoutManager = LinearLayoutManager(this.activity)
        binding.mSrCountryList?.let { mSRCountryList = it }
        bottomSheet = view.findViewById(R.id.mBottomSheetFilter)
        bottomSheetFragmentBinding = BottomSheetFragmentBinding.bind(bottomSheet)
        sheetBehavior = from(bottomSheet)
        sheetBehavior.peekHeight = PEEK_HEIGHT
        bottomSheetFragmentBinding?.textStartRangeArea?.let { mTextMinArea = it }
        bottomSheetFragmentBinding?.textEndRangeArea?.let { mTextMaxArea = it }
        bottomSheetFragmentBinding?.textStartRangePopulation?.let { mTextMinPopulation = it }
        bottomSheetFragmentBinding?.textEndRangePopulation?.let { mTextMaxPopulation = it }
        bottomSheetFragmentBinding?.peekIcon?.let { headerPeek = it }
        bottomSheetFragmentBinding?.mEIDistance?.let { mEditText = it }
        bottomSheetFragmentBinding?.rangeSliderArea?.let { mRSliderArea = it }
        bottomSheetFragmentBinding?.rangeSliderPopulation?.let { mRSliderPopulation = it }

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)

    }

    fun setStartStateOfBottomSheet() {
        mFilterRepository.processNewQuery("")
        val currentListCountries = mAdapter.getCurrentListCountries()
        if (currentListCountries.size > 1) {
            val maxArea = currentListCountries.maxOf { it.convertToCorrectArea().toFloat() }
            val minArea = currentListCountries.minOf { it.convertToCorrectArea().toFloat() }
            val maxPopulation = currentListCountries.maxOf { it.population.toFloat() }
            val minPopulation = currentListCountries.minOf { it.population.toFloat() }
            mRSliderArea.setValues(minArea, maxArea)
            mRSliderPopulation.setValues(minPopulation, maxPopulation)
            mTextMinArea.text = String.format("%5.0f km2", minArea)
            mTextMaxArea.text = String.format("%5.0f km2", maxArea)
            mTextMinPopulation.text =  String.format("%5.0f ppl", minPopulation)
            mTextMaxPopulation.text = String.format("%5.0f ppl", maxPopulation)
        } else {
            mRSliderArea.setValues(MIN_AREA, MAX_AREA)
            mRSliderPopulation.setValues(MIN_POPULATION, MAX_POPULATION)
            mTextMinArea.text = String.format("%5.0f km2", MIN_AREA)
            mTextMaxArea.text = String.format("%5.0f km2", MAX_AREA)
            mTextMinPopulation.text = String.format("%5.0f ppl", MIN_POPULATION)
            mTextMaxPopulation.text = String.format("%5.0f ppl", MAX_POPULATION)
        }
        mEditText.setText("")
    }
}












