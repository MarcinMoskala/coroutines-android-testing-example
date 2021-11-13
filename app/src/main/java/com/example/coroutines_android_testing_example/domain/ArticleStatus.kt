package com.example.coroutines_android_testing_example.domain

data class ArticleStatus(
    val articleKey: String,
    val articleStatus: Status
) {
    enum class Status {
        UNSEEN, SEEN, READ
    }
}