package ru.ermolnik.news

import ru.mts.data.news.repository.News

data class NewsViewState(val id: Int, val title: String, val body: String)

internal fun News.toViewState(): NewsViewState {
    return NewsViewState(id, title, body)
}
