package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.lavyshyk.countrycity.R
import com.lavyshyk.domain.dto.CapitalDto

class SearchResultAdapter (
    private val layoutInflater: LayoutInflater,
    private val onClickItemSearch: (CapitalDto) -> Unit
) : BaseAdapter() {

    private val mCapitals = mutableListOf<CapitalDto>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = layoutInflater.inflate(R.layout.fragment_item_capital, parent, false)
        itemView.findViewById<TextView>(R.id.mTvCapital).text = mCapitals[position].capital
        itemView.setOnClickListener { onClickItemSearch(mCapitals[position]) }
        return itemView
    }

    override fun getItem(position: Int) = mCapitals[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = mCapitals.size

    fun setResultData(capitals: MutableList<CapitalDto>) {
        this.mCapitals.clear()
        this.mCapitals.addAll(capitals)
        notifyDataSetChanged()
    }
}