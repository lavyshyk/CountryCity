package com.lavyshyk.domain.useCase.implementetion.netCase.flow

import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlow
import com.lavyshyk.domain.useCase.implementetion.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetNewsFromApiFlowUseCase(private val mNewsNetworkRepositoryOnFlow: NewsNetworkRepositoryOnFlow) :
FlowUseCase<String, Flow<Outcome<List<ArticleDto>>>>()
{
    override fun buildFlow(params: String?): Flow<Outcome<List<ArticleDto>>> =
        mNewsNetworkRepositoryOnFlow.getNews(params?: "")


    override val mIsParamsRequired: Boolean
        get() = true
}