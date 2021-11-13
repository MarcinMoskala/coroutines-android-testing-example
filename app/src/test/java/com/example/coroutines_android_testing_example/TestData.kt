package com.example.coroutines_android_testing_example

import com.example.coroutines_android_testing_example.domain.Article
import org.joda.time.DateTime

object TestData {
    val aTime1 = DateTime.parse("2010-06-20T01:20+02:00")
    val aTime2 = DateTime.parse("2010-06-21T01:20+02:00")
    val aTime3 = DateTime.parse("2010-06-22T01:20+02:00")
    val aTime4 = DateTime.parse("2010-06-23T01:20+02:00")
    val anArticle = Article("someKey", "Some title", "Some description", "http://some.pl/link", "http://someUrl.pl/link", aTime1)
    val anArticle2 = Article("someKey2", "Some title 2", "Some description 2", "http://some.pl/link2", "http://someUrl.pl/link2", aTime2)
    val anArticle3 = Article("someKey3", "Some title 3", "Some description 3", "http://some.pl/link3", "http://someUrl.pl/link3", aTime3)
    val anArticle4 = Article("someKey4", "Some title 4", "Some description 4", "http://some.pl/link4", "http://someUrl.pl/link4", aTime4)
}