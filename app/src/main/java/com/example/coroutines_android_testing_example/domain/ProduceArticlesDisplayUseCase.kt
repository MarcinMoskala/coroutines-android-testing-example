package com.example.coroutines_android_testing_example.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ProduceArticlesDisplayUseCase(
    private val networkArticleRepositoryComposite: ArticleRepository,
    private val articleStatusRepository: ArticleStatusRepository,
) {
    suspend fun produce(): List<ArticleDisplay> = coroutineScope {
        val articles = async { networkArticleRepositoryComposite.fetchArticles() }
        val articleStatuses = articleStatusRepository.getArticleStatuses()
            .associateBy { it.articleKey }

        articles.await()
            .map {
                ArticleDisplay(
                    article = it,
                    status = articleStatuses[it.key]?.articleStatus
                        ?: ArticleStatus.Status.UNSEEN
                )
            }
    }
}