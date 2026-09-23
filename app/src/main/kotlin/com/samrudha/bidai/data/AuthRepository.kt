package com.samrudha.bidai.data

import com.samrudha.bidai.data.remote.ApiError
import com.samrudha.bidai.data.remote.AuthApi
import com.samrudha.bidai.data.remote.AuthResponse
import com.samrudha.bidai.data.remote.LoginRequest
import com.samrudha.bidai.data.remote.SignupRequest
import java.io.IOException
import kotlinx.serialization.SerializationException

class AuthRepository(
    private val api: AuthApi,
    private val tokenStore: TokenStore
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> =
        runCatchingResult { api.login(LoginRequest(email = email, password = password)) }

    suspend fun signup(name: String, mobile: String, email: String, password: String): Result<AuthResponse> =
        runCatchingResult {
            api.signup(
                SignupRequest(
                    name = name,
                    mobile = mobile.trim(),
                    email = email,
                    password = password
                )
            )
        }

    private suspend fun runCatchingResult(
        call: suspend () -> AuthResponse
    ): Result<AuthResponse> {
        return try {
            val response = call()
            tokenStore.saveSession(response.user, response.token)
            Result.success(response)
        } catch (e: retrofit2.HttpException) {
            Result.failure(Exception(parseError(e) ?: "Something went wrong, please try again"))
        } catch (e: SerializationException) {
            Result.failure(Exception("Something went wrong, please try again"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error, please check your connection"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseError(e: retrofit2.HttpException): String? {
        return try {
            val body = e.response()?.errorBody()?.string() ?: return null
            NetworkModule.json.decodeFromString<ApiError>(body).error
        } catch (_: Exception) {
            null
        }
    }
}
