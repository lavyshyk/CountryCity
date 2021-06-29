package com.lavyshyk.countrycity.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.CountryApi
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.data.CountryData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
binding fragment by inflater
 */
class ListFragment : Fragment() {
    lateinit var  recycler: RecyclerView
//    private var fragmentListBinding: FragmentListBinding? = null
//    private val binding = fragmentListBinding!!

    private lateinit var mAdapter:CountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        fragmentListBinding = FragmentListBinding.inflate(inflater,container,false)
//        return binding.root
        return inflater.inflate(R.layout.fragment_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          //binding.recView.layoutManager = LinearLayoutManager(activity)
        recycler = view.findViewById(R.id.rec_view)
        recycler.layoutManager = LinearLayoutManager(activity)
        getResultRequest()
    }

//    override fun onDestroyView() {
//        fragmentListBinding = null
//        super.onDestroyView()
//    }

    private fun getResultRequest() = CountryApi.retrofitService.getCountryInfo().enqueue(object :
        Callback<MutableList<CountryData>>{
        override fun onResponse(
            call: Call<MutableList<CountryData>>,
            response: Response<MutableList<CountryData>>
        ) {
            val responseBody = response.body() as MutableList<CountryData>
            mAdapter = CountryAdapter(responseBody)
            mAdapter.notifyDataSetChanged()
            recycler.adapter = mAdapter
            //binding.recView.adapter = mAdapter


        }

        override fun onFailure(call: Call<MutableList<CountryData>>, t: Throwable) {
            t.printStackTrace()

        }

    })
}



