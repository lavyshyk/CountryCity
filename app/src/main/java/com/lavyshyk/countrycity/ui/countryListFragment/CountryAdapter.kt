package com.lavyshyk.countrycity.ui.countryListFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.adapter.BaseAdapter
import com.lavyshyk.countrycity.dto.CountryDto

class CountryAdapter : BaseAdapter<CountryDto>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCountry: AppCompatTextView = view.findViewById(R.id.textViewCountry)
        val tvCapital: AppCompatTextView = view.findViewById(R.id.textViewCapital)
        val tvLanguages: AppCompatTextView = view.findViewById(R.id.textViewLang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            val item = mDataList[position]
            holder.tvCountry.text = item.name
            holder.tvCapital.text = holder.itemView.context.getString(R.string.capital_is, item.capital)
            holder.itemView.setOnClickListener { mClickFunction?.invoke(item) }
            //val strLang = list.languages?.joinToString { it.name }
            // holder.tvLanguages.text = holder.itemView.context.getString(R.string.languages,strLang)
        }
    }

    fun repopulateSorted(list: MutableList<CountryDto>) {
        list.sortBy { it.area }
        repopulate(list)
    }

    fun repopulateDescendingSorted(list: MutableList<CountryDto>) {
        list.sortByDescending { it.area }
        repopulate(list)
    }

    fun sortAndReplaceItem() {
        mDataList.sortBy { it.area }
        notifyDataSetChanged()
    }

    fun sortDescendingAndReplaceItem() {
        mDataList.sortByDescending { it.area }
        notifyDataSetChanged()
    }

}