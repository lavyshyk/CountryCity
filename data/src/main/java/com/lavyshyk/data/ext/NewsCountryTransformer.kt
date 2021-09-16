package com.lavyshyk.data.ext

import com.lavyshyk.data.model.news.NewsCountry
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Transformer

class NewsCountryTransformer : Transformer<NewsCountry, List<ArticleDto>> {
    override var convert: (NewsCountry) -> List<ArticleDto> =
        { data ->
            data.articles.map { article ->
                ArticleDto(
                    article.author ?: "",
                    article.description ?: "",
                    article.publishedAt ?: "",
                    article.title ?: "",
                    article.url ?: "",
                    article.urlToImage ?: ""
                )
            }
        }
}