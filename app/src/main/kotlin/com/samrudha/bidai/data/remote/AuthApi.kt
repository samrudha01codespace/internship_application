package com.samrudha.bidai.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/signup")
    suspend fun signup(@Body body: SignupRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse
}
