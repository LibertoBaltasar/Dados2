package com.example.dados2.chucknorris

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChuckNorrisApi {
    @GET("jokes/random")
    suspend fun getRandomJoke(): Call<Chiste>

    @GET("jokes/random?category=")
    suspend fun getJokeByCategory(@Query("category") category: String): Chiste
}