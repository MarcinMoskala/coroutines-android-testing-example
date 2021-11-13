package com.example.coroutines_android_testing_example.ui.main

import android.view.View
import android.widget.TextView
import com.example.coroutines_android_testing_example.R
import com.example.coroutines_android_testing_example.domain.ArticleDisplay
import com.workshop.universityannouncementsboard.util.ItemAdapter

class ArticleItemAdapter(
    private val articleDisplay: ArticleDisplay
) : ItemAdapter(R.layout.article_item_view) {

    override fun setupView(containerView: View) {
        containerView.findViewById<TextView>(R.id.textView)
            .text = articleDisplay.article.title
    }
}