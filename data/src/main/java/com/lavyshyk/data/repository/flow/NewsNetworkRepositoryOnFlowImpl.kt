package com.lavyshyk.data.repository.flow

import com.lavyshyk.data.ext.modifyFlow
import com.lavyshyk.data.model.news.NewsCountry
import com.lavyshyk.data.network.NewsServiceFlow
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class NewsNetworkRepositoryOnFlowImpl(
    private val service: NewsServiceFlow,
    private val transformer: Transformer<NewsCountry, List<ArticleDto>>,
    private val dispatcher: CoroutineDispatcher
) : NewsNetworkRepositoryOnFlow {

    override fun getNewsByCountryCode(countryCode: String): Flow<Outcome<List<ArticleDto>>> =
        modifyFlow(service.getNewsByCountryCode(countryCode),transformer,dispatcher)

    override fun getNewsByCountryName(countryName: String): Flow<Outcome<List<ArticleDto>>> =
        modifyFlow(service.getNewsByCountryCode(countryName),transformer,dispatcher)
}