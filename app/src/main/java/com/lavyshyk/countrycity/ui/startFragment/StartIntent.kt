package com.simple.mvi.features.home

import com.lavyshyk.countrycity.base.mvi.ViewIntent

/**
 * Created by Rim Gazzah on 8/26/20.
 **/
sealed class StartIntent : ViewIntent {

    data class LoadNews(val countryCode: String) : StartIntent()
    data class Exception(val t: Throwable): StartIntent()

}