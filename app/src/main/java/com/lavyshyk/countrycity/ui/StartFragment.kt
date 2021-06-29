package com.lavyshyk.countrycity.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.databinding.FragmentStartBinding

/*
binding fragment by constructor
 */
class StartFragment : Fragment(R.layout.fragment_start) {
    private var fragmentStartBinding: FragmentStartBinding? = null
    //private val nameCountry: CountryData = CountryData("AAAAA")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStartBinding.bind(view)
        fragmentStartBinding = binding
       // binding.mCountry = nameCountry
    }

    override fun onDestroyView() {
        fragmentStartBinding = null
        super.onDestroyView()
    }
}