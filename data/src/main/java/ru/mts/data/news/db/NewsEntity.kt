package ru.mts.data.news.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mts.data.news.repository.News

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "body") val body: String,
)

fun NewsEntity.toDomain() = News(
    id = this.id,
    title = this.title,
    body = this.body,
)

fun News.toEntity(): NewsEntity = NewsEntity(
    id = this.id,
    title = this.title,
    body = this.body,
)