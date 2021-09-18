package com.simple.mvi.features.home

import com.lavyshyk.countrycity.base.mvi.ViewAction

/**
 * Created by Rim Gazzah on 8/26/20.
 **/
sealed class StartAction : ViewAction {
    data class GetNewsByCode(val countryCode: String) : StartAction()
    object GetNews : StartAction()
    data class Fail(val t: Throwable) : StartAction()
}