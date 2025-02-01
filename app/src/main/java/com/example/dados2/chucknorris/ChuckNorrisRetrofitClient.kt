package com.example.dados2.chucknorris

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChuckNorrisRetrofitClient {
    private const val BASE_URL = "https://api.chucknorris.io/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}