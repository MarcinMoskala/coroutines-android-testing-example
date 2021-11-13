package com.example.coroutines_android_testing_example.domain

interface ArticleStatusRepository {
    suspend fun getArticleStatuses(): List<ArticleStatus>
    suspend fun addNewArticleKeys(articleKeys: List<String>)
    suspend fun changeArticleStatus(articleStatus: ArticleStatus)
}