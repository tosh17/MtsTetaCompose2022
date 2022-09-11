package ru.ermolnik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.news.repository.NewsWithTypeSource
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState.Loading)
    val state = _state.asStateFlow()
    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    init {
        viewModelScope.launch {
            getNews(false)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getNews(true)
        }
    }

    private suspend fun getNews(forceUpdate: Boolean) {
        viewModelScope.launch {
            repository.getNews(forceUpdate).collect(::updateView)
        }
    }

    private suspend fun updateView(result: Result<NewsWithTypeSource, Throwable>) {
        result.doOnError { error ->
            _state.emit(NewsState.Error(error))
            _isRefreshing.value = false
        }.doOnSuccess { (news, isLocal) ->
            val viewState = news.map { it.toViewState() }
            _state.emit(
                if (isLocal) {
                    NewsState.FromBd(viewState)
                } else {
                    NewsState.FromNet(viewState)
                }
            )
            _isRefreshing.value = false
        }
    }

    fun onClickTryAgain() {
        viewModelScope.launch {
            _state.emit(NewsState.Loading)
            getNews(true)
        }
    }
}
