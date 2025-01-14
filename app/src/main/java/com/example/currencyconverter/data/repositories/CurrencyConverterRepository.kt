package com.example.currencyconverter.data.repositories

import com.example.currencyconverter.data.models.ConvertError
import com.example.currencyconverter.data.models.ConvertResponse
import com.example.currencyconverter.data.remote.ApiInterface
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScoped
class CurrencyConverterRepository @Inject constructor(
    private val api: ApiInterface
) {

    suspend fun getCurrencies() : Result<Map<String, String>> {
        return try {
            val response = api.getCurrencies()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected Error: ${e.message}", e))
        }
    }

    suspend fun convert(value: Double, from: String, to: String): Result<ConvertResponse> {
        return try {
            val response = api.convert(value, from, to)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val convertError = try {
                    Gson().fromJson(errorBody, ConvertError::class.java)
                } catch (e: Exception) {
                    ConvertError(
                        error = true,
                        status = response.code(),
                        message = "Unknown Error",
                        description = "Failed to parse error response"
                    )
                }
                Result.failure(Exception("Error ${convertError.status}: ${convertError.message} - ${convertError.description}"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected Error: ${e.message}", e))
        }
    }

}