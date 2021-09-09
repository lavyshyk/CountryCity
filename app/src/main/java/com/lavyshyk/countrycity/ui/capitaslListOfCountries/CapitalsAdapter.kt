package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.domain.dto.CapitalDto

class CapitalsAdapter :
    ListAdapter<CapitalDto, CapitalsAdapter.ListViewHolder>(DifferItemCallback()) {

    class DifferItemCallback : DiffUtil.ItemCallback<CapitalDto>() {
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CapitalDto, newItem: CapitalDto): Boolean {
            return oldItem === newItem
        }

        override fun areItemsTheSame(oldItem: CapitalDto, newItem: CapitalDto): Boolean {
            return oldItem == newItem
        }
    }

    inner class ListViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        var mTvCapital: AppCompatTextView = containerView.findViewById(R.id.mTvCapital)
        fun bind(item: CapitalDto) {

            if (item.capital != "") {
                mTvCapital.text = item.capital
            } else {
                mTvCapital.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_capital, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}