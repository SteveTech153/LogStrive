package com.example.logstrive

import retrofit2.http.GET

interface QuoteApi {
    @GET("api/random")
    suspend fun getQuote(): List<Quote>
}