package com.lavyshyk.countrycity.ui.countryListFragment

import android.content.Context
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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvvm.IBaseMvvmView
import com.lavyshyk.countrycity.databinding.BottomSheetFragmentBinding
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import com.lavyshyk.countrycity.util.checkLocationPermission
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.FilterRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.scope.Scope


class CountriesListFragment : ScopeFragment(), IBaseMvvmView {


    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var mProgress: FrameLayout
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var bottomSheetFragmentBinding: BottomSheetFragmentBinding? = null
    private var leftPopulation: Float = MIN_POPULATION
    private var rightPopulation: Float = MAX_POPULATION
    private var leftArea: Float = MIN_AREA
    private var rightArea: Float = MAX_AREA
    private val mViewModel: CountryListViewModel by stateViewModel()
    private val mFilterRepository: FilterRepository by inject()
    private lateinit var headerPeek: AppCompatImageView
    private var bundle = Bundle()
    private lateinit var mEditText: AppCompatEditText
    private lateinit var mRSliderArea: RangeSlider
    private lateinit var mRSliderPopulation: RangeSlider
    private lateinit var mTextMaxArea: AppCompatTextView
    private lateinit var mTextMinArea: AppCompatTextView
    private lateinit var mTextMaxPopulation: AppCompatTextView
    private lateinit var mTextMinPopulation: AppCompatTextView
    override val scope: Scope
        get() = super.scope


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (context?.checkLocationPermission() == true) {
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
                .requestLocationUpdates(mLocationRequest, mLocationCallback, null);
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

        mProgress = binding.mPBarList
        mAdapter = CountryAdapter()
        binding.recView.adapter = mAdapter
        binding.recView.layoutManager = LinearLayoutManager(this.activity)
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
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)

        mViewModel.getCountryListFromDB()
        mViewModel.getCountriesFromApi()


        mViewModel.mSortedCountryList.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                    showProgress()
                }
                is Outcome.Next -> {
                    hideProgress()
                    showCountryData(it.data)
                }
                is Outcome.Failure -> {
                    hideProgress()
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
                    showProgress()
                }
                is Outcome.Next -> {
                    showCountryData(it.data)
                    hideProgress()
                }
                is Outcome.Failure -> {
                    hideProgress()

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
                        bundle
                    )
                    mViewModel.putSharedPrefString(COUNTRY_NAME_FOR_NAV_KEY, item.name)
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
                mTextMinArea.text = leftArea.toString()
                mTextMaxArea.text = rightArea.toString()
                mFilterRepository.processNewArea(leftArea, rightArea)
            })
        mRSliderPopulation.addOnChangeListener(
            RangeSlider.OnChangeListener { slider, value, fromUser ->
                leftPopulation = slider.values[0]
                rightPopulation = slider.values[1]
                mTextMinPopulation.text = leftPopulation.toString()
                mTextMaxPopulation.text = rightPopulation.toString()
                mFilterRepository.processNewPopulation(leftPopulation, rightPopulation)
            })
    }

    override fun onDestroyView() {
        bottomSheetFragmentBinding = null
        fragmentListBinding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_sort_and_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (mViewModel.getSharedPrefBoolean(ITEM_SORT_STATUS)) {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_big)
                .isChecked = mViewModel.getSharedPrefBoolean(ITEM_SORT_STATUS)
            mAdapter.sortAndReplaceItem()
        } else {
            menu.findItem(R.id.sortCountries)
                .setIcon(R.drawable.ic_action_list_sort_to_small)
                .isChecked = mViewModel.getSharedPrefBoolean(ITEM_SORT_STATUS)
            mAdapter.sortDescendingAndReplaceItem()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.searchCountry -> {
            var search = ""
            activity?.showDialogQuickSearch(
                "Search country", R.string.no, null,
                R.string.yes, {
                    search =
                        bundle.getString(COUNTRY_NAME_KEY_FOR_DIALOG, "").toString().lowercase()
                    mFilterRepository.processNewQuery(search)
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
                mViewModel.putSharedPrefBoolean(ITEM_SORT_STATUS, item.isChecked)
                item.setIcon(R.drawable.ic_action_list_sort_to_big)
            } else {
                mAdapter.sortAndReplaceItem()
                item.isChecked = SORT_TO_SMALL
                mViewModel.putSharedPrefBoolean(ITEM_SORT_STATUS, item.isChecked)
                item.setIcon(R.drawable.ic_action_list_sort_to_small)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showCountryData(countries: MutableList<CountryDto>) {
        countries.let {
            if (mViewModel.getSharedPrefBoolean(ITEM_SORT_STATUS)) {
                mAdapter.repopulateSorted(it)
            } else {
                mAdapter.repopulateDescendingSorted(it)
            }
        }
    }

    fun setStartStateOfBottomSheet() {
        mFilterRepository.processNewQuery("")
        val currentListCountries = mAdapter.getCurrentListCountries()
        if (currentListCountries.size > 1) {
            val maxArea = currentListCountries.maxOf { it.area }
            val minArea = currentListCountries.minOf { it.area }
            val maxPopulation = currentListCountries.maxOf { it.population.toFloat() }
            val minPopulation = currentListCountries.minOf { it.population.toFloat() }
            mRSliderArea.setValues(minArea, maxArea)
            mRSliderPopulation.setValues(minPopulation, maxPopulation)
            mTextMinArea.text = minArea.toString()
            mTextMaxArea.text = maxArea.toString()
            mTextMinPopulation.text = minPopulation.toString()
            mTextMaxPopulation.text = maxPopulation.toString()
        } else {

            mRSliderArea.setValues(MIN_AREA, MAX_AREA)
            mRSliderPopulation.setValues(MIN_POPULATION, MAX_POPULATION)
            mTextMinArea.text = MIN_AREA.toString()
            mTextMaxArea.text = MAX_AREA.toString()
            mTextMinPopulation.text = MIN_POPULATION.toString()
            mTextMaxPopulation.text = MAX_POPULATION.toString()
        }
        mEditText.setText("")
    }

    override fun showError(error: String, throwable: Throwable) {

        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        mProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgress.visibility = View.GONE
    }

    val mLocationRequest: LocationRequest = LocationRequest.create().apply {
        this.interval = 60000
        this.fastestInterval = 5000
        this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    mViewModel.putCurrentLocation(location)
                }
            }
        }
    }
}












