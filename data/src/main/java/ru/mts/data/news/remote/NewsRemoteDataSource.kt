package ru.mts.data.news.remote

import kotlinx.coroutines.delay
import ru.mts.data.main.NetworkClient
import ru.mts.data.utils.Result
import ru.mts.data.utils.runOperationCatching

class NewsRemoteDataSource {
    suspend fun getNews(): Result<List<NewsDto.Response>, Throwable> {
        return runOperationCatching {
            delay(3000L)
            NetworkClient.create().getSampleDataList()
        }
    }
}