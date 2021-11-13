package com.example.coroutines_android_testing_example.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutines_android_testing_example.domain.ArticleDisplay
import com.example.coroutines_android_testing_example.domain.ProduceArticlesDisplayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val produceArticlesDisplayUseCase: ProduceArticlesDisplayUseCase,
) : ViewModel() {

    private val _progressBarVisible = MutableStateFlow(false)
    val progressBarVisible: StateFlow<Boolean> = _progressBarVisible

    private val _articlesListState = MutableStateFlow<ArticlesListState>(ArticlesListState.Initial)
    val articlesListState: StateFlow<ArticlesListState> = _articlesListState

    fun onCreate() {
        viewModelScope.launch {
            _progressBarVisible.value = true
            val articles = produceArticlesDisplayUseCase.produce()
            _articlesListState.value = ArticlesListState.ArticlesLoaded(articles)
            _progressBarVisible.value = false
        }
    }
}

sealed class ArticlesListState {
    object Initial : ArticlesListState()
    data class ArticlesLoaded(val articles: List<ArticleDisplay>) : ArticlesListState()
}

