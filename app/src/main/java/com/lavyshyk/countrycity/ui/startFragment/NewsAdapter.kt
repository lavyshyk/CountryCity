package com.lavyshyk.countrycity.ui.startFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.base.adapter.BaseAdapter
import com.lavyshyk.countrycity.util.loadJpgFlag
import com.lavyshyk.domain.dto.news.ArticleDto

class NewsAdapter : BaseAdapter<ArticleDto>() {

    class NewsViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

        val mTvTitle: AppCompatTextView = containerView.findViewById(R.id.mTvTitleNews)
        val mTvDescription: AppCompatTextView = containerView.findViewById(R.id.mTvDescriptionNews)
        val mIvNews: AppCompatImageView = containerView.findViewById(R.id.mIvNews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_news_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            val item = mDataList[position]

            holder.mTvTitle.text = item.title
            holder.mTvDescription.text = item.description
            holder.mIvNews.loadJpgFlag(item.urlToImage)
        }
    }
}




