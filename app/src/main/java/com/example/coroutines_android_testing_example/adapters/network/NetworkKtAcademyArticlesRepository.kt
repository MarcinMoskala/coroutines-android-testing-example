package com.example.coroutines_android_testing_example.adapters

import adapters.network.makeRetrofit
import com.example.coroutines_android_testing_example.domain.Article
import com.example.coroutines_android_testing_example.domain.ArticleRepository
import org.joda.time.DateTime
import retrofit2.http.GET
import retrofit2.http.Header

class NetworkKtAcademyArticlesRepository : ArticleRepository {
    private val api = makeRetrofit("https://api.kt.academy/")
        .create(Api::class.java)

    override suspend fun fetchArticles(): List<Article> =
        api.fetchArticles(null)
            .map { it.toArticle() }

    interface Api {
        @GET("article")
        suspend fun fetchArticles(@Header("userUuid") userUuid: String?): List<KtAcademyArticleJson>
    }
}

fun KtAcademyArticleJson.toArticle() = Article(
    key = "kta-$key",
    title = title,
    description = shortDescription,
    imageUrl = thumbnailUrl,
    linkUrl = "https://kt.academy/article/$key",
    publishedAt = publicationDate
)

data class KtAcademyArticleJson(
    val key: String,
    val title: String,
    val contentMd: String?,
    val shortDescription: String,
    val thumbnailUrl: String,
    val author: PublicUserJson?,
    val publicationDate: DateTime,
    val reviewers: List<PublicUserJson>,
    val prev: PrevNextDescription?,
    val next: PrevNextDescription?,
)

data class PublicUserJson(
    val publicKey: String?,
    val imageUrl: String,
    val displayName: String,
    val bio: String?,
    val bioPl: String?,
    val socialMedia: SocialMediaJson,
    val trainer: UserTrainerJson?,
    val tags: List<String>,
)

data class PrevNextDescription(
    val key: String,
)

data class SocialMediaJson(
    val websiteUrl: String?,
    val twitter: String?,
    val github: String?,
    val linkedin: String?,
)

data class UserTrainerJson(
    val topics: List<String>,
    val description: String,
    val experienceDescription: String,
    val shortDescription: String,
)