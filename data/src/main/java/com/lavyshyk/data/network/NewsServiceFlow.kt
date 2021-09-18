package com.lavyshyk.data.network

import com.lavyshyk.data.model.news.NewsCountry
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsServiceFlow {

    @GET("top-headlines")
    fun getNewsByCountryCode(@Query("country") countryCode: String): Flow<NewsCountry>

    @GET("everything?sources=bbc-news")
    fun getNews(): Flow<NewsCountry>
}