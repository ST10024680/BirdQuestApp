package com.example.birdwatchapp_p2.ui.home

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BirdApiService {
    @GET("ref/hotspot/{regionCode}")
    fun getHotspots(
        @Path("regionCode") regionCode: String,
        @Query("fmt") format: String = "json",
        @Query("key") apiKey: String
    ): Call<List<Hotspot>>
}

