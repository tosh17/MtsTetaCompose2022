package ru.mts.data.news.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.utils.Result
import ru.mts.data.utils.runOperationCatching

class NewsLocalDataSource(private val context: Context) {
    suspend fun getNews(): Result<List<NewsEntity>, Throwable> {
        return runOperationCatching {
            delay(500L)
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().getAll()
            }
        }
    }

    suspend fun save(news: NewsEntity) {
        withContext(Dispatchers.IO) {
            AppDatabase.getDatabase(context).newsDao().insert(news)
        }
    }

    suspend fun saveAll(news: List<NewsEntity>) {
        withContext(Dispatchers.IO) {
            AppDatabase.getDatabase(context).newsDao().insertAll(news)
        }
    }
}
