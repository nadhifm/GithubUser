package com.nadhif.githubuser.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nadhif.githubuser.BuildConfig
import com.nadhif.githubuser.network.response.DetailResponse
import com.nadhif.githubuser.network.response.SearchResponse
import com.nadhif.githubuser.network.response.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.github.com/"
const val TOKEN = BuildConfig.TOKEN

interface GithubApiService {

    @GET("users")
    @Headers("Authorization: token $TOKEN")
    fun getUsersAsync(): Deferred<List<User>>

    @GET("search/users")
    @Headers("Authorization: token $TOKEN")
    fun searchUserAsync(@Query("q") query: String): Deferred<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    fun getUserDetailAsync(@Path("username") username: String): Deferred<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN")
    fun getFollowersAsync(@Path("username") username: String): Deferred<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN")
    fun getFollowingAsync(@Path("username") username: String): Deferred<List<User>>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object GithubApi {
    val retrofitService: GithubApiService by lazy { retrofit.create(GithubApiService::class.java) }
}