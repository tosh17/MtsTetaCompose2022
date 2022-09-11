package ru.mts.data.news.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.db.toDomain
import ru.mts.data.news.db.toEntity
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.remote.toDomain
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess
import ru.mts.data.utils.mapSuccess

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) {
    suspend fun getNews(forceUpdate: Boolean = false): Flow<Result<NewsWithTypeSource, Throwable>> {
        return flow {
            if (forceUpdate) {
                emit(getRemote())
            } else {
                getLocal()
                    .doOnSuccess { result ->
                        if (result.news.isEmpty()) {
                            emit(getRemote())
                        } else {
                            emit(Result.Success(result))
                        }
                    }
                    .doOnError { emit(getRemote()) }
            }
        }
    }


    private suspend fun getLocal(): Result<NewsWithTypeSource, Throwable> {
        return newsLocalDataSource.getNews()
            .mapSuccess { result ->
                NewsWithTypeSource(
                    result.map { it.toDomain() }, true
                )
            }
    }

    private suspend fun getRemote(): Result<NewsWithTypeSource, Throwable> {
        return newsRemoteDataSource.getNews()
            .mapSuccess { result ->
                NewsWithTypeSource(result.map { it.toDomain() }, false)
            }.doOnSuccess { saveNews(it.news) }
    }

    private suspend fun saveNews(news: List<News>) {
        if (news.isNotEmpty()) {
            newsLocalDataSource.saveAll(news.map { it.toEntity() })
        }
    }
}
