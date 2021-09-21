package com.lavyshyk.domain.repository.flow

import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow


interface NewsNetworkRepositoryOnFlowDagger {

    fun getNewsByCountryCodeDagger(countryCode: String): Flow<Outcome<List<ArticleDto>>>

    fun getNewsDagger(): Flow<Outcome<List<ArticleDto>>>
}