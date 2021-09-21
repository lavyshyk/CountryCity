package com.simple.mvi.features.home

import com.lavyshyk.countrycity.base.mvi.ViewState
import com.lavyshyk.domain.dto.news.ArticleDto

/**
 * Created by Rim Gazzah on 8/26/20.
 **/
sealed class StartState : ViewState {
    data class Loading(val loading: Boolean) : StartState()
    data class ResultNews(val data : List<ArticleDto>): StartState()
    data class Exception(val t: Throwable) : StartState()
}