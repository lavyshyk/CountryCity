package com.lavyshyk.countrycity.di.dagger.module

import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import com.lavyshyk.countrycity.KEY_HEADER
import com.lavyshyk.countrycity.VALUE_HEADER
import com.lavyshyk.data.BASE_URL_NEWS
import com.lavyshyk.data.network.NewsServiceFlow
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideLogging() = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BASIC
    )

    @Provides
    @Singleton
    fun providesOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
            .writeTimeout(6, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor{ chain ->
                val request = chain.request().newBuilder()
                    .addHeader(KEY_HEADER, VALUE_HEADER)
                    .build()
                chain.proceed(request)
            }
        return client.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiServiceNews(retrofit: Retrofit) : NewsServiceFlow {
        return  retrofit.create(NewsServiceFlow::class.java)
    }
}