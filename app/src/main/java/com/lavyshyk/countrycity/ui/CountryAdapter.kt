package com.lavyshyk.countrycity.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.data.CountryData
import com.lavyshyk.countrycity.ui.CountryAdapter.ViewHolder

class CountryAdapter(dataCountry: MutableList<CountryData>): RecyclerView.Adapter<ViewHolder>() {

    private var data = dataCountry

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val  tvCountry: AppCompatTextView = view.findViewById(R.id.textViewCountry)
        val  tvCapital: AppCompatTextView = view.findViewById(R.id.textViewCapital)
        val  tvLanguages: AppCompatTextView = view.findViewById(R.id.textViewLang)
        //val  tv3alpha: AppCompatTextView = view.findViewById(R.id.text_3alpha)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = data[position]
        holder.tvCountry.text = list.name
        //  holder.tv3alpha.text = list.alpha3Code
        holder.tvCapital.text = holder.itemView.context.getString(R.string.capital_is,list.capital)
        val strLang = list.languages.let { it.joinToString(", ") {it.name} }
        holder.tvLanguages.text = holder.itemView.context.getString(R.string.languages,strLang)
    }

    override fun getItemCount(): Int {
        return try {
            data.size
        } catch (e: Exception) {
            0
        }
    }

}