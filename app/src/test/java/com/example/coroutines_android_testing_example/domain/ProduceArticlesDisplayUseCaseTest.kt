package com.example.coroutines_android_testing_example.domain

import com.example.coroutines_android_testing_example.TestData.anArticle
import com.example.coroutines_android_testing_example.TestData.anArticle2
import com.example.coroutines_android_testing_example.domain.ArticleStatus.Status.READ
import com.example.coroutines_android_testing_example.domain.ArticleStatus.Status.UNSEEN
import com.example.coroutines_android_testing_example.fakes.FakeArticleRepository
import com.example.coroutines_android_testing_example.fakes.FakeArticleStatusRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProduceArticlesDisplayUseCaseTest {
    private val articleRepository = FakeArticleRepository(listOf(anArticle, anArticle2))
    private val articleStatusRepository = FakeArticleStatusRepository(listOf(ArticleStatus(anArticle2.key, READ)))

    @Test
    fun `should produce article display`() = runBlockingTest {
        // given
        val useCase = ProduceArticlesDisplayUseCase(articleRepository, articleStatusRepository)

        // when
        val articlesDisplay = useCase.produce()

        // then
        assertEquals(
            listOf(ArticleDisplay(anArticle, UNSEEN), ArticleDisplay(anArticle2, READ)),
            articlesDisplay
        )
    }

    // ...
}