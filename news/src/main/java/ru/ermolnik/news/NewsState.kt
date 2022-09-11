package ru.ermolnik.news

sealed class NewsState {
    object Loading : NewsState()
    data class Error(val throwable: Throwable) : NewsState()
    data class FromNet(val news: List<NewsViewState>) : NewsState()
    data class FromBd(val news: List<NewsViewState>) : NewsState()
}