package ru.mts.data.news.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NewsApiService {
    @GET("api/v1/samplelist")
    @Headers("Content-Type:application/json; charset=utf-8;")
    suspend fun getSampleDataList(): List<NewsDto.Response>

    @POST("api/v1/sample")
    @Headers("Content-Type:application/json; charset=utf-8;")
    suspend fun getSampleData(@Body request: NewsDto.Request): NewsDto.Response

    @POST("login")
    @Headers("Content-Type:application/json; charset=utf-8;")
    suspend fun login(): Boolean
}
