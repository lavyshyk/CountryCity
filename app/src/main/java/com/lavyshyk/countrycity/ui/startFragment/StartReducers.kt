package com.simple.mvi.features.home

import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome

/**
 * Created by Rim Gazzah on 8/31/20.
 **/

fun Outcome<List<ArticleDto>>.reduce(isSearchMode: Boolean = false): StartState {
    return when (this) {
        is Outcome.Success ->  StartState.ResultNews(data)
        is Outcome.Failure -> StartState.Exception(t)
        is Outcome.Progress -> StartState.Loading(loading)
        is Outcome.Next -> StartState.ResultNews(data)
    }
}