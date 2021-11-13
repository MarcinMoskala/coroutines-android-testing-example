package com.example.coroutines_android_testing_example.adapters

import adapters.network.makeRetrofit
import com.example.coroutines_android_testing_example.domain.Article
import com.example.coroutines_android_testing_example.domain.ArticleRepository
import com.prof.rssparser.Parser
import org.joda.time.format.DateTimeFormat
import retrofit2.http.GET

class NetworkKotlinBlogRepository : ArticleRepository {
    private val api = makeRetrofit("https://blog.jetbrains.com/")
        .create(Api::class.java)
    private val parser = Parser.Builder().build()

    override suspend fun fetchArticles(): List<Article> =
        api.fetchRss()
            .let { parser.parse(it) }
            .articles
            .mapNotNull {
                Article(
                    key = "kb-" + (it.link
                        ?.substringBeforeLast("/")
                        ?.substringAfterLast("/")
                        ?: return@mapNotNull null),
                    title = it.title ?: return@mapNotNull null,
                    description = it.description.orEmpty(),
                    imageUrl = null, // TODO
                    linkUrl = it.link ?: return@mapNotNull null,
                    publishedAt = it.pubDate.let { PUB_DATE_FORMAT.parseDateTime(it) }
                )
            }

    interface Api {
        @GET("kotlin/feed")
        suspend fun fetchRss(): String
    }

    companion object {
        val PUB_DATE_FORMAT = DateTimeFormat.forPattern("E, d MMM yyyy H:m:s Z")
    }
}