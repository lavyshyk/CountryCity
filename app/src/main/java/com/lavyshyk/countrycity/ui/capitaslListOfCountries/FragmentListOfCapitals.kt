package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lavyshyk.countrycity.base.mvvm.IBaseMvvmView
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.countrycity.databinding.FragmentListCapitalsBinding
import com.lavyshyk.domain.dto.CapitalDto
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.scope.Scope

class FragmentListOfCapitals : ScopeFragment(), IBaseMvvmView {
    private var fragmentListCapitalsBinding: FragmentListCapitalsBinding? = null
    private lateinit var binding: FragmentListCapitalsBinding
    private lateinit var mCapitalAdapter: CapitalsAdapter
    private lateinit var mProgress: FrameLayout
    private val mViewModel: CapitalListViewModel by stateViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // fragmentListCapitalsBinding =
        binding = FragmentListCapitalsBinding.inflate(inflater, container, false)
        // binding = fragmentListCapitalsBinding as @NonNull FragmentListCapitalsBinding
        return binding.root
    }

    override val scope: Scope
        get() = super.scope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recViewCapital.layoutManager = LinearLayoutManager(this.activity)
        mCapitalAdapter = CapitalsAdapter()
        binding.recViewCapital.adapter = mCapitalAdapter
        binding.mPBarListCapitals.let { mProgress = it }
        mViewModel.getListOfCapitals()
        mViewModel.mCapitalList.observe(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> {
                    if (it.loading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
                is Outcome.Next -> {
                    //mCapitalAdapter.addList(it.data)
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
        }


    }

    override fun onDestroyView() {
        fragmentListCapitalsBinding = null
        super.onDestroyView()
    }


    private fun showCountryData(capitals: MutableList<CapitalDto>) {
        capitals.let {

            mCapitalAdapter.repopulate(capitals)
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
}