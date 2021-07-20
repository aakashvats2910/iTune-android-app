package org.kashisol.musik.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIReceiver {

    @GET("search?")
    fun getSearchResponse(@Query("term") term: String): Call<JsonObject>

}