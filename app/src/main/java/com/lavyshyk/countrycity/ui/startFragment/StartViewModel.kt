package com.lavyshyk.countrycity.ui.startFragment

import com.lavyshyk.countrycity.base.mvi.BaseViewModel
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import com.simple.mvi.features.home.reduce
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@InternalCoroutinesApi
class StartViewModel @Inject constructor(
    private val mNewsNetworkRepositoryOnFlowForDaggerImpl: NewsNetworkRepositoryOnFlowDagger
) : BaseViewModel<StartIntent, StartAction, StartState>() {
    override fun intentToAction(intent: StartIntent): StartAction {
        return when (intent) {
            is StartIntent.LoadNewsByCode -> StartAction.GetNewsByCode(intent.countryCode)
            is StartIntent.LoadNews -> StartAction.GetNews
            is StartIntent.Exception -> StartAction.Fail(intent.t)
        }
    }

    override fun handleAction(action: StartAction) {
        launchOnUI {
            when (action) {
                is StartAction.GetNewsByCode -> {
                    mNewsNetworkRepositoryOnFlowForDaggerImpl
                        .getNewsByCountryCodeDagger(action.countryCode.lowercase())
                        .collect { mState.value = it.reduce() }
                }
                is StartAction.GetNews -> {
                    mNewsNetworkRepositoryOnFlowForDaggerImpl.getNewsDagger()
                        .collect { mState.value = it.reduce() }
                }
                is StartAction.Fail -> {
                    mState.value = (Outcome.failure<List<ArticleDto>>(action.t)).reduce()
                }
            }
        }
    }
}
