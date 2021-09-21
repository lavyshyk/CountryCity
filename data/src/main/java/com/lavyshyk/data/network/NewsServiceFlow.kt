package com.lavyshyk.data.network

import com.lavyshyk.data.API_NEWS_KEY
import com.lavyshyk.data.model.news.NewsCountry
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsServiceFlow {

    @Headers(API_NEWS_KEY)
    @GET("v2/top-headlines")
    fun getNews(@Query("country") alpha_2_ISO_3166_1: String): Flow<NewsCountry>
}