package com.example.coroutines_android_testing_example

import android.app.Application
import com.example.coroutines_android_testing_example.adapters.NetworkKotlinBlogRepository
import com.example.coroutines_android_testing_example.adapters.NetworkKtAcademyArticlesRepository
import com.example.coroutines_android_testing_example.adapters.database.ArticleStatusDatabase
import com.example.coroutines_android_testing_example.domain.ArticleRepository
import com.example.coroutines_android_testing_example.domain.ArticleStatusRepository
import com.example.coroutines_android_testing_example.domain.ArticlesRepositoryComposite
import com.example.coroutines_android_testing_example.domain.ProduceArticlesDisplayUseCase
import com.example.coroutines_android_testing_example.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class ArticlesClientApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            androidContext(this@ArticlesClientApplication)
            modules(module {
                single<ArticleRepository>(named("kta")) { NetworkKtAcademyArticlesRepository() }
                single<ArticleRepository>(named("kotlin_blog")) { NetworkKotlinBlogRepository() }
                single<ArticleRepository> {
                    ArticlesRepositoryComposite(
                        listOf(get(named("kta")), get(named("kotlin_blog")))
                    )
                }
                single<ArticleStatusRepository> { ArticleStatusDatabase(androidContext()) }
                single { ProduceArticlesDisplayUseCase(get(), get()) }
                viewModel { MainViewModel(get()) }
            })
        }
    }
}