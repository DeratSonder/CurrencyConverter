package com.example.currencyconverter.data.remote

import com.example.currencyconverter.data.models.ConvertResponse
import com.example.currencyconverter.utilities.Constants.API_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/currencies.json")
    suspend fun getCurrencies(
        @Query("prettyprint") prettyPrint: Boolean = false,
        @Query("show_alternative") showAlternative: Boolean = false,
        @Query("show_inactive") showInactive: Boolean = false,
        @Header("Authorization") authorization: String = API_KEY
    ): Response<Map<String, String>>

    @GET("/convert")
    suspend fun convert(
        @Path("value") value: Double,
        @Path("from") from: String,
        @Path("to") to: String,
        @Query("app_id") appId: String = API_KEY,
        @Query("prettyprint") prettyPrint: Boolean = false
    ): Response<ConvertResponse>

}