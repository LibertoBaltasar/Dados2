package com.example.dados2.chucknorris

data class Chiste(
    val icon_url: String,
    val id: String,
    val url: String,
    val value: String
)

data class SearchResult(
    val total: Int,
    val result: List<Chiste>
)