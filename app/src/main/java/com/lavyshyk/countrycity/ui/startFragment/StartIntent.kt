package com.simple.mvi.features.home

import com.lavyshyk.countrycity.base.mvi.ViewIntent

/**
 * Created by Rim Gazzah on 8/26/20.
 **/
sealed class StartIntent : ViewIntent {

    object LoadNewsByCode : StartIntent()
    object LoadNews : StartIntent()
    data class Exception(val t: Throwable): StartIntent()

}