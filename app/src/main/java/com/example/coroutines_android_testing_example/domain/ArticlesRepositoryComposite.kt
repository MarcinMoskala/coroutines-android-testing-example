package com.example.coroutines_android_testing_example.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class ArticlesRepositoryComposite(
    private val articleRepositories: List<ArticleRepository>,
) : ArticleRepository {
    override suspend fun fetchArticles(): List<Article> = supervisorScope {
        articleRepositories
            .map { async { it.fetchArticles() } }
            .mapNotNull {
                try {
                    it.await()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    null
                }
            }
            .flatten()
            .sortedByDescending { it.publishedAt }
    }
}