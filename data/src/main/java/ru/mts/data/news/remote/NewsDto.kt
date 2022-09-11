package ru.mts.data.news.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import ru.mts.data.news.repository.News


class NewsDto {
    @Parcelize
    data class Request(@SerializedName("id") val id: Int) : Parcelable

    @Parcelize
    data class Response(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String
    ) : Parcelable
}

internal fun NewsDto.Response.toDomain(): News {
    return News(
        id = this.id,
        title = this.title,
        body = this.body,
    )
}
