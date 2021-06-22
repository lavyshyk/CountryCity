package com.lavyshyk.countrycity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recycler: RecyclerView = view.findViewById(R.id.rec_view)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = CountryAdapter()
        return view
    }



}