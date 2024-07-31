package com.example.logstrive.data.network

import com.example.logstrive.data.entity.Quote
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/random")
    suspend fun getQuote(): List<Quote>
}