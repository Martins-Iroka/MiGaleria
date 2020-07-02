package com.martdev.android.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.domain.videomodel.VideoData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL = "https://api.pexels.com/"
const val SEARCH_PHOTO = "v1/search"
const val CURATED_PHOTO = "/v1/curated"
const val SEARCH_VIDEO = "/videos/search"
const val POPULAR_VIDEO = "/videos/popular"

interface ApiService {

    @Headers("Authorization : 563492ad6f91700001000001eb6a1798e655484a977965987ff92e9d")
    @GET(SEARCH_PHOTO)
    fun searchPhotoAsync(@Query("query") query: String? = null,
                         @Query("per_page") per_page: Int? = null,
                         @Query("page") page: Int? = null):
            Deferred<Response<PhotoData>>

    @Headers("Authorization : 563492ad6f91700001000001eb6a1798e655484a977965987ff92e9d")
    @GET(CURATED_PHOTO)
    fun loadPhotoAsync(@Query("per_page") per_page: Int,
                       @Query("page") page: Int): Deferred<Response<PhotoData>>

    @Headers("Authorization : 563492ad6f91700001000001eb6a1798e655484a977965987ff92e9d")
    @GET(SEARCH_VIDEO)
    fun searchVideoAsync(@Query("query") query: String? = null,
                         @Query("per_page") per_page: Int? = null,
                         @Query("page") page: Int? = null):
            Deferred<Response<VideoData>>

    @Headers("Authorization : 563492ad6f91700001000001eb6a1798e655484a977965987ff92e9d")
    @GET(POPULAR_VIDEO)
    fun loadVideoAsync(@Query("per_page") per_page: Int,
                       @Query("page") page: Int): Deferred<Response<VideoData>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun getApiService(): ApiService = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build().create(ApiService::class.java)

