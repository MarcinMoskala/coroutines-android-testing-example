package com.example.coroutines_android_testing_example.fakes

import com.example.coroutines_android_testing_example.domain.Article
import com.example.coroutines_android_testing_example.domain.ArticleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class FakeArticleRepository(
    private val articles: List<Article>,
    private val delayTime: Long = 1000
) : ArticleRepository {

    override suspend fun fetchArticles(): List<Article> {
        delay(delayTime)
        return articles
    }
}

class FailingArticleRepository(val throwable: Throwable) : ArticleRepository {

    override suspend fun fetchArticles(): List<Article> = throw throwable
}

class FakeCancellationAwareArticleRepository(
    private val articles: List<Article>,
    private val delayTime: Long = 1000
) : ArticleRepository {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    var cancelled = false
        private set

    override suspend fun fetchArticles(): List<Article> = suspendCancellableCoroutine {
        it.invokeOnCancellation { cancelled = true }
        scheduler.schedule({ it.resume(articles) }, delayTime, TimeUnit.MILLISECONDS)
    }
}