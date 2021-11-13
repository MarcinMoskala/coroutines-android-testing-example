package com.example.coroutines_android_testing_example.domain

import org.joda.time.DateTime

data class Article(
    val key: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val linkUrl: String,
    val publishedAt: DateTime,
)

data class ArticleDisplay(
    val article: Article,
    val status: ArticleStatus.Status
)