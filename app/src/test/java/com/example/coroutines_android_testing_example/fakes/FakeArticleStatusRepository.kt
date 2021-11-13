package com.example.coroutines_android_testing_example.fakes

import com.example.coroutines_android_testing_example.domain.ArticleStatus
import com.example.coroutines_android_testing_example.domain.ArticleStatus.Status.UNSEEN
import com.example.coroutines_android_testing_example.domain.ArticleStatusRepository

class FakeArticleStatusRepository(
    private var articleStatuses: List<ArticleStatus> = emptyList(),
    private val delayTime: Long = 1000
) : ArticleStatusRepository {

    override suspend fun getArticleStatuses(): List<ArticleStatus> = articleStatuses

    override suspend fun addNewArticleKeys(articleKeys: List<String>) {
        articleStatuses = articleKeys.map { ArticleStatus(it, UNSEEN) } + articleStatuses
    }

    override suspend fun changeArticleStatus(articleStatus: ArticleStatus) {
        val oldStatus = articleStatuses.find { it.articleKey == articleStatus.articleKey } ?: return
        articleStatuses = articleStatuses - oldStatus + articleStatus
    }
}

class FailingArticleStatusRepository(val throwable: Throwable) : ArticleStatusRepository {
    override suspend fun getArticleStatuses(): List<ArticleStatus> = throw throwable

    override suspend fun addNewArticleKeys(articleKeys: List<String>) = throw throwable

    override suspend fun changeArticleStatus(articleStatus: ArticleStatus) = throw throwable
}