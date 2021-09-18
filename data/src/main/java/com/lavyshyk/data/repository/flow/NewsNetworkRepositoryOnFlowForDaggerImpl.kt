package com.lavyshyk.data.repository.flow

import com.lavyshyk.data.ext.modifyFlowForDagger
import com.lavyshyk.data.model.news.NewsCountry
import com.lavyshyk.data.network.NewsServiceFlow
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import kotlinx.coroutines.flow.Flow

class NewsNetworkRepositoryOnFlowForDaggerImpl(
    private val service: NewsServiceFlow,
    private val transformer: Transformer<NewsCountry, List<ArticleDto>>,

    ) : NewsNetworkRepositoryOnFlowDagger {

    override fun getNewsByCountryCodeDagger(countryCode: String): Flow<Outcome<List<ArticleDto>>> =
        modifyFlowForDagger(service.getNewsByCountryCode(countryCode), transformer)


    override fun getNewsDagger(): Flow<Outcome<List<ArticleDto>>> =
        modifyFlowForDagger(service.getNews(), transformer)

}
