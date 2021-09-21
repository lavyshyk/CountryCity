package com.lavyshyk.countrycity.ui.countryDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.adapter.BaseAdapter
import com.lavyshyk.domain.dto.country.LanguageDto

class LanguageAdapter : BaseAdapter<LanguageDto>() {
    class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTVLanguage: AppCompatTextView = view.findViewById(R.id.mTVLanguage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_language,parent,false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LanguageViewHolder){
            val item = mDataList[position]
            holder.mTVLanguage.text = item.name
        }
    }
}