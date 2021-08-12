package com.lavyshyk.countrycity.ui.countryListFragment


//import com.lavyshyk.countrycity.CountryApp.Companion.database
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.countrycity.databinding.BottomSheetFragmentBinding
import com.lavyshyk.countrycity.databinding.FragmentListBinding
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.repository.database.DataBaseRepository
import com.lavyshyk.countrycity.repository.filter.CountryFilter
import com.lavyshyk.countrycity.repository.filter.FilterRepository
import com.lavyshyk.countrycity.ui.ext.showDialogQuickSearch
import com.lavyshyk.countrycity.util.checkLocationPermission
import com.lavyshyk.countrycity.util.transformEntitiesToCountryDto
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel


/*
binding fragment by inflater
 */
class CountriesListFragment : ScopeFragment()
//   : BaseMpvFragment<ICountryListView, CountryListPresenter>(), ICountryListView
{

    private var fragmentListBinding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var mAdapter: CountryAdapter
    private lateinit var mProgress: FrameLayout
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var bottomSheetFragmentBinding: BottomSheetFragmentBinding? = null
    private var leftPopulation: Float = 0.0f
    private var rightPopulation: Float = 0.0f
    private var leftArea: Float = 0.0f
    private var rightArea: Float = 0.0f
    private val mViewModel: CountryListViewModel by stateViewModel()
    private val mFilterRepository: FilterRepository by inject()
    private val mDataBaseRepository: DataBaseRepository by inject()
    private lateinit var behaviorSubject: BehaviorSubject<CountryFilter>
    private lateinit var headerPeek: AppCompatImageView
    private var bundle = Bundle()


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

        mViewModel.getCountriesInfoApi()

        behaviorSubject = mFilterRepository.getFilterSubject()

        mViewModel.mDataBase.observe(
            viewLifecycleOwner,
            { it ->
                if (it.isNullOrEmpty()) {
                    showCountryData(mutableListOf<CountryDto>())
                } else {
                    showCountryData(it.transformEntitiesToCountryDto())
                }
            })

        mProgress = binding.mPBarList

        mAdapter = CountryAdapter()
        mAdapter.setItemClick { item ->
            bundle.putString(COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(
                R.id.action_listFragment_to_countryDetailsFragment,
                bundle
            )
            mViewModel.putSharedPrefString(COUNTRY_NAME_FOR_NAV_KEY, item.name)        }
        binding.recView.adapter = mAdapter
        binding.recView.layoutManager = LinearLayoutManager(this.activity)

        mViewModel.mMyFilter.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                }
                is Outcome.Next -> {
                    if (it.data.name == ""){
                        mViewModel.getSortedListCountry(it.data)
                    }else{
                        mViewModel.getCountryListSearchByName(it.data.name)
                    }

                }
                is Outcome.Failure -> {
                    Snackbar.make(
                        binding.root, getString(R.string.some_wrong), Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Outcome.Success -> {

                }
            }

        })

        mViewModel.mSortedCountryList.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                    mProgress.visibility = View.VISIBLE
                }
                is Outcome.Next -> {
                    mProgress.visibility = View.GONE
                    showCountryData(it.data)
                }
                is Outcome.Failure -> {
                    mProgress.visibility = View.GONE
                    Snackbar.make(
                        binding.root, getString(R.string.wrong_county), Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Outcome.Success -> {
                    mProgress.visibility = View.GONE

                }
            }
        })

        mViewModel.mCountyLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress -> {
                    mProgress.visibility = View.VISIBLE
                }
                is Outcome.Next -> {
                    val unit = mDataBaseRepository.getListCountryName().count()
                    if (unit > 0) {
                        mDataBaseRepository.updateListCountry(it.data)
//                        database?.countryDao()
//                            ?.updateListCountry(it.data.transformEntitiesToCountry())
                    } else {
                        mDataBaseRepository.saveListCountry(it.data)
//                        database?.countryDao()
//                            ?.saveListCountry(it.data.transformEntitiesToCountry())
                    }
                    mProgress.visibility = View.GONE
                }
                is Outcome.Failure -> {
                    mProgress.visibility = View.GONE
                    Snackbar.make(
                        binding.root, getString(R.string.wrong_county), Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Outcome.Success -> {
                    mProgress.visibility = View.GONE
                }
            }
        })


        // bottomSheet
        bottomSheet = view.findViewById(R.id.mBottomSheetFilter)

        bottomSheetFragmentBinding = BottomSheetFragmentBinding.bind(bottomSheet)

        sheetBehavior = from(bottomSheet)

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager

        sheetBehavior.peekHeight = PEEK_HEIGHT
        headerPeek = bottomSheetFragmentBinding?.peekIcon!!
        headerPeek.setOnClickListener {
            if (sheetBehavior.state == STATE_EXPANDED) {
                sheetBehavior.state = STATE_COLLAPSED
                imm.hideSoftInputFromWindow(
                    headerPeek.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
                );
            } else {
                sheetBehavior.state = STATE_EXPANDED

            }
        }

        val mEditText = bottomSheetFragmentBinding?.mEIDistance

        val mRSliderArea = bottomSheetFragmentBinding?.rangeSliderArea
        val mRSliderPopulation = bottomSheetFragmentBinding?.rangeSliderPopulation

        sheetBehavior.addBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_EXPANDED -> {
                            mRSliderArea?.setValues(0f, 17_124_442F)
                            mRSliderPopulation?.setValues(0f, 1_377_422_166F)
                            mEditText?.setText("")
                        }
                        STATE_COLLAPSED -> {
                            if (mEditText?.text.isNullOrEmpty()){
                                mFilterRepository.processNewDistance(Float.MAX_VALUE)
                            }else{
                                mFilterRepository.processNewDistance(mEditText?.text.toString().toFloat())
                            }

                            mViewModel.getCountryFilter(behaviorSubject)
                            sheetBehavior.peekHeight = PEEK_HEIGHT
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

        mRSliderArea?.addOnChangeListener(
            RangeSlider.OnChangeListener
            { slider, value, fromUser ->
                leftArea = slider.values[0]
                rightArea = slider.values[1]
                mFilterRepository.processNewArea(leftArea,rightArea)
            })
        mRSliderPopulation?.addOnChangeListener(
            RangeSlider.OnChangeListener
            { slider, value, fromUser ->
                leftPopulation = slider.values[0]
                rightPopulation = slider.values[1]
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
            var s = ""
            activity?.showDialogQuickSearch(
                "Search country", R.string.no, null,
                R.string.yes, {
                    s = bundle.getString(COUNTRY_NAME_KEY_FOR_DIALOG, "").toString().lowercase()

                    mFilterRepository.processNewQuery(s)
                    mViewModel.getCountryFilter(behaviorSubject)
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
        mViewModel.putSharedPrefBoolean(ITEM_SORT_STATUS, status)
    }

    private fun getSortStatus(): Boolean = mViewModel.getSharedPrefBoolean(ITEM_SORT_STATUS)


    private fun showCountryData(countries: MutableList<CountryDto>) {

        countries.let {
            if (getSortStatus()) {
                mAdapter.repopulateSorted(it)
            } else {
                mAdapter.repopulateDescendingSorted(it)
            }
        }
    }


    val mLocationRequest = LocationRequest.create()
        .setInterval(60000)
        .setFastestInterval(5000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                return
            }
            for (location: Location in locationResult.locations) {
                if (location != null) {
                    location
                    mViewModel.putCurrentLocation(location)
                }
            }
        }

    }
}









