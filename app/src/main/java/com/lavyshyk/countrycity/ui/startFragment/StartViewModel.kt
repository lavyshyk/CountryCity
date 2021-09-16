package com.lavyshyk.countrycity.ui.startFragment

import com.lavyshyk.countrycity.base.mvi.BaseViewModel
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.useCase.implementetion.netCase.flow.GetNewsFromApiFlowUseCase
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import com.simple.mvi.features.home.reduce
import kotlinx.coroutines.flow.collect

class StartViewModel(
    private val mGetNewsFromApiFlowUseCase: GetNewsFromApiFlowUseCase
    //useCases
) : BaseViewModel<StartIntent, StartAction, StartState>() {
    override fun intentToAction(intent: StartIntent): StartAction {
        return when (intent) {
            is StartIntent.LoadNews -> StartAction.GetNews(intent.countryCode)
            is StartIntent.Exception -> StartAction.Fail(intent.t)
        }
    }

    override fun handleAction(action: StartAction) {
        launchOnUI {
            when (action) {
                is StartAction.GetNews -> {
                    mGetNewsFromApiFlowUseCase.setParams(action.countryCode.lowercase()).execute().collect{
                        mState.postValue(it.reduce())
                    }
                }
                is StartAction.Fail -> {
                    mState.postValue((Outcome.failure<List<ArticleDto>>(action.t)).reduce())
                }
            }
        }
    }
}