package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.mvvm.IBaseMvvmView
import com.lavyshyk.countrycity.databinding.FragmentListCapitalsBinding
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class FragmentListOfCapitals : ScopeFragment(), IBaseMvvmView {
    private var fragmentListCapitalsBinding: FragmentListCapitalsBinding? = null
    private lateinit var binding: FragmentListCapitalsBinding
    private lateinit var mCapitalAdapter: CapitalsAdapter
    private lateinit var mProgress: FrameLayout
    private val bundle = Bundle()
    private val mViewModel: CapitalListViewModel by stateViewModel()
    private val mSearchResultAdapter by lazy {
        SearchResultAdapter(
            layoutInflater,
            ::onClickItemSearch
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListCapitalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recViewCapital.layoutManager = LinearLayoutManager(this.activity)
        mCapitalAdapter = CapitalsAdapter()
        binding.recViewCapital.adapter = mCapitalAdapter
        binding.mPBarListCapitals.let { mProgress = it }
        binding.searchList.adapter = mSearchResultAdapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recViewCapital)

        // mViewModel.getListOfCapital()
//        mViewModel.mCapitalList.observe(viewLifecycleOwner,
//            {
//                when (it) {
//                    is OutcomeCoroutine.Running -> {
//                        if (it.loading) {
//                            showProgress()
//                        } else {
//                            hideProgress()
//                        }
//                    }
//                    is OutcomeCoroutine.Result -> {
//
//                        showCountryData(it.data)
//                    }
//                    is OutcomeCoroutine.Cancel -> {
//                        showError(it.c.message.toString(), it.c)
//                    }
//                    is OutcomeCoroutine.FailureCor -> {
//                        showError(it.t.message.toString(), it.t)
//                    }
//                }
//            })

        mViewModel.getListOfCapitalsByFlow().asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Failure -> {
                        showError(it.t.message.toString(), it.t)
                    }
                    is Outcome.Next -> {
                    }
                    is Outcome.Progress -> {
                        if (it.loading) {
                            showProgress()
                        } else {
                            hideProgress()
                        }
                    }
                    is Outcome.Success -> {
                        showCountryData(it.data)
                    }
                }
            })

    }


    private fun showCountryData(capitals: MutableList<CapitalDto>) {
        capitals.let {
            mCapitalAdapter.submitList(capitals.toList())

        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool_bar_with_search_view, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val mSearch = menu.findItem(R.id.searchView)
        val mSearchView = mSearch.actionView as SearchView
        mSearchView.queryHint = context?.getString(R.string.enter_your_keyword)
        mSearch.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                binding.recViewCapital.alpha = 0.2f
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                mSearchResultAdapter.setResultData(mutableListOf())
                binding.recViewCapital.alpha = 1.0f
                return true
            }
        })

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.length > 2) {
                        requestSearch(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.length > 2) {
                        requestSearch(newText)
                    }
                }
                return false
            }
        })
    }

    fun requestSearch(text: String) {
        mViewModel.setFilter(text)
        mViewModel.mFilteredStream.asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Failure -> {
                        showError(it.t.message.toString(), it.t)
                    }
                    is Outcome.Next -> {
                    }
                    is Outcome.Progress -> {
                        if (it.loading) {
                            showProgress()
                        } else {
                            hideProgress()
                        }
                    }
                    is Outcome.Success -> {
                        mSearchResultAdapter.setResultData(it.data)
                    }
                }
            })
    }

    private fun requestCountryNameByCapital(capitalDto: CapitalDto) {
        mViewModel.setFilter(capitalDto)
        mViewModel.mFilteredCountry.asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Failure -> {
                        showError(it.t.message.toString(), it.t)
                    }
                    is Outcome.Next -> {
                    }
                    is Outcome.Progress -> {
                        if (it.loading) {
                            showProgress()
                        } else {
                            hideProgress()
                        }
                    }
                    is Outcome.Success -> {
                        bundle.putString(COUNTRY_NAME_KEY, it.data[0].name.lowercase())
                        findNavController().navigate(R.id.action_capitalsList_to_mapCountiesFragment, bundle)
                    }
                }
            })

    }

    private fun onClickItemSearch(capitalDto: CapitalDto) {
        requestCountryNameByCapital(capitalDto)
    }

    override fun onDestroyView() {
        fragmentListCapitalsBinding = null
        super.onDestroyView()
    }
}
