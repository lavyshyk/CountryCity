package com.lavyshyk.domain.repository.flow

import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow


interface NewsNetworkRepositoryOnFlow {

    fun getNews(countryCode: String): Flow<Outcome<List<ArticleDto>>>
}