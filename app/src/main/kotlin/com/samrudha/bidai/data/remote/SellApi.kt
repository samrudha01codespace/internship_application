package com.samrudha.bidai.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SellApi {
    @GET("api/categories")
    suspend fun getCategories(): CategoriesResponse

    @Multipart
    @POST("api/uploads")
    suspend fun uploadImages(
        @Part images: List<MultipartBody.Part>
    ): UploadsResponse

    @POST("api/products")
    suspend fun createProduct(
        @Body body: CreateProductRequest
    ): CreateProductResponse
}
