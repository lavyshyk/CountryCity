package com.lavyshyk.data.model.news

data class NewsCountry(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>
    )

