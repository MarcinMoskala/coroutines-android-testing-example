package com.example.coroutines_android_testing_example.domain

interface ArticleRepository {
    suspend fun fetchArticles(): List<Article>
}