package ru.mts.data.news.repository

data class NewsWithTypeSource(val news: List<News>, val isLocal: Boolean)