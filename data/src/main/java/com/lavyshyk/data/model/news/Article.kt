package com.lavyshyk.data.model.news

data class Article(
    val author: String?,
//    val content: Any?,
    val description: String?,
    val publishedAt: String?,
//    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
