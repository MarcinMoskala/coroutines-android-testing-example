package com.example.coroutines_android_testing_example.domain

import com.example.coroutines_android_testing_example.TestData.anArticle
import com.example.coroutines_android_testing_example.TestData.anArticle2
import com.example.coroutines_android_testing_example.TestData.anArticle3
import com.example.coroutines_android_testing_example.TestData.anArticle4
import com.example.coroutines_android_testing_example.fakes.FailingArticleRepository
import com.example.coroutines_android_testing_example.fakes.FakeArticleRepository
import com.example.coroutines_android_testing_example.fakes.FakeCancellationAwareArticleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ArticlesRepositoryCompositeTest {
    private val articleRepository1 = FakeArticleRepository(listOf(anArticle2))
    private val articleRepository2 = FakeArticleRepository(listOf(anArticle, anArticle3))
    private val articleRepository3 = FakeArticleRepository(listOf(anArticle4))
    private val failingArticleRepository = FailingArticleRepository(IOException("Network Exception"))

    @Test
    fun `should load data from all repositories and sort them by publication date`() = runBlockingTest {
        // given
        val compositeRepository = ArticlesRepositoryComposite(listOf(articleRepository1, articleRepository2, articleRepository3))

        // when
        val articles = compositeRepository.fetchArticles()

        // then
        assertEquals(listOf(anArticle4, anArticle3, anArticle2, anArticle), articles)
    }

    @Test
    fun `should load data from different repositories asynchronously`() = runBlockingTest {
        // given
        val compositeRepository = ArticlesRepositoryComposite(listOf(articleRepository1, articleRepository2))

        // when
        val articles = compositeRepository.fetchArticles()

        // then
        assertEquals(1000, currentTime)
    }

    @Test
    fun `should load other repositories when one has an exception`() = runBlockingTest {
        // given
        val compositeRepository = ArticlesRepositoryComposite(listOf(articleRepository1, failingArticleRepository, articleRepository2))

        // when
        val articles = compositeRepository.fetchArticles()

        // then
        assertEquals(listOf(anArticle3, anArticle2, anArticle), articles)
    }

    @Test
    fun `should propagate cancel to children`() = runBlockingTest {
        // given
        val cancellationAwareRepository = FakeCancellationAwareArticleRepository(listOf(anArticle, anArticle3))
        val compositeRepository = ArticlesRepositoryComposite(listOf(articleRepository1, cancellationAwareRepository))

        // when
        val job = launch { compositeRepository.fetchArticles() }
        delay(100)
        job.cancel()

        // then
        assertTrue(cancellationAwareRepository.cancelled)
    }
}

