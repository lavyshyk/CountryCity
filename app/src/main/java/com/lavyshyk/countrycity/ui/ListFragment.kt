package com.lavyshyk.countrycity.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lavyshyk.countrycity.databinding.FragmentListBinding

/*
binding fragment by inflater
 */
class ListFragment : Fragment() {
    //private lateinit var recycler: RecyclerView
    private var fragmentListBinding: FragmentListBinding? = null
    private val binding = fragmentListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListBinding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recView.layoutManager = LinearLayoutManager(activity)
        binding.recView.adapter = CountryAdapter()
//        recycler = view.findViewById(R.id.rec_view)
//        recycler.layoutManager = LinearLayoutManager(activity)
//        recycler.adapter = CountryAdapter()
    }

    override fun onDestroyView() {
        fragmentListBinding = null
        super.onDestroyView()
    }
}