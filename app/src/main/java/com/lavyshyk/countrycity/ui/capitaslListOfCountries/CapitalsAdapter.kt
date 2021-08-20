package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.adapter.BaseAdapter
import com.lavyshyk.countrycity.ui.countryDetails.LanguageAdapter
import com.lavyshyk.domain.dto.CapitalDto

class CapitalsAdapter: BaseAdapter<CapitalDto>() {

    class CapitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTVCapital: AppCompatTextView = view.findViewById(R.id.mTVCapital)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_capital,parent,false)
        return LanguageAdapter.LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CapitalsAdapter.CapitalViewHolder){
            val item = mDataList[position]
            holder.mTVCapital.text = item.capital
        }
    }


}