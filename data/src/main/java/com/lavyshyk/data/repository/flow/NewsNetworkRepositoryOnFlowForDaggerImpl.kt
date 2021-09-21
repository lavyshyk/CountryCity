package com.lavyshyk.data.repository.flow

import com.lavyshyk.data.model.news.NewsCountry
import com.lavyshyk.data.network.NewsServiceFlow
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform

class NewsNetworkRepositoryOnFlowForDaggerImpl(
    private val service: NewsServiceFlow,
    private val transformer: Transformer<NewsCountry, List<ArticleDto>>,

    ) : NewsNetworkRepositoryOnFlowDagger {

    override fun getNewsByCountryCodeDagger(countryCode: String): Flow<Outcome<List<ArticleDto>>> {
       return service.getNewsByCountryCode(countryCode).
               transform { value: NewsCountry ->
                   emit(Outcome.loading(true))
                   emit(Outcome.success(transformer.convert(value)))
                   emit(Outcome.loading(false))
               }.catch{ ex -> emit(Outcome.failure(ex))}
    }

        //modifyFlowForDagger(service.getNewsByCountryCode(countryCode), transformer)


    override fun getNewsDagger(): Flow<Outcome<List<ArticleDto>>> {
        return service.getNews()
            .transform { value: NewsCountry ->
            emit(Outcome.loading(true))
            emit(Outcome.success(transformer.convert(value)))
            emit(Outcome.loading(false))
        }.catch { ex -> emit(Outcome.failure(ex)) }

    }
    //modifyFlowForDagger(service.getNews(), transformer)

}
