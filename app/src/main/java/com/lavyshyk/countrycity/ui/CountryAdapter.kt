package com.lavyshyk.countrycity.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.ui.CountryAdapter.ViewHolder

class CountryAdapter(): RecyclerView.Adapter<ViewHolder>() {
//    lateinit var fragmentListBinding: FragmentListBinding
//    lateinit var fragmentItemBinding: FragmentItemBinding
//    var binding: FragmentItemBinding = fragmentItemBinding

    private val data = listOf<String>("Англия","Беларусь","Венгрия","Дания")



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val  textView: AppCompatTextView = view.findViewById(R.id.textView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //fragmentItemBinding = FragmentItemBinding.bind(parent)
        //return ViewHolder(fragmentItemBinding.root)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item,parent,false)
       return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position]

    }

    override fun getItemCount(): Int {
        return data.size
    }
}