package com.example.coroutines_android_testing_example.ui.main

import com.example.coroutines_android_testing_example.TestData.anArticle
import com.example.coroutines_android_testing_example.TestData.anArticle2
import com.example.coroutines_android_testing_example.TestData.anArticle3
import com.example.coroutines_android_testing_example.domain.ArticleDisplay
import com.example.coroutines_android_testing_example.domain.ArticleStatus
import com.example.coroutines_android_testing_example.domain.ArticleStatus.Status.READ
import com.example.coroutines_android_testing_example.domain.ArticleStatus.Status.UNSEEN
import com.example.coroutines_android_testing_example.domain.ProduceArticlesDisplayUseCase
import com.example.coroutines_android_testing_example.fakes.FakeArticleRepository
import com.example.coroutines_android_testing_example.fakes.FakeArticleStatusRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class MainViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val articleRepository = FakeArticleRepository(listOf(anArticle3, anArticle))
    private val articleStatusRepository = FakeArticleStatusRepository(listOf(ArticleStatus(anArticle.key, READ)))
    private val produceArticlesDisplayUseCase = ProduceArticlesDisplayUseCase(
        networkArticleRepositoryComposite = articleRepository,
        articleStatusRepository = articleStatusRepository,
    )
    private val viewModel = MainViewModel(
        produceArticlesDisplayUseCase = produceArticlesDisplayUseCase,
    )

    @Test
    fun `should show progress bar when loading data`() {
        // given
        assertFalse(viewModel.progressBarVisible.value)

        // when
        viewModel.onCreate()

        // then
        assertTrue(viewModel.progressBarVisible.value)

        // when
        mainCoroutineRule.advanceUntilIdle()

        // then
        assertFalse(viewModel.progressBarVisible.value)
    }

    @Test
    fun `should load articles list`() {
        // given
        assertEquals(ArticlesListState.Initial, viewModel.articlesListState.value)

        // when
        viewModel.onCreate()
        mainCoroutineRule.advanceUntilIdle()

        // then
        assertEquals(
            ArticlesListState.ArticlesLoaded(listOf(ArticleDisplay(anArticle3, UNSEEN), ArticleDisplay(anArticle, READ))),
            viewModel.articlesListState.value
        )

        // and should load all the data concurrently
        assertEquals(1000, mainCoroutineRule.currentTime)
    }
}

