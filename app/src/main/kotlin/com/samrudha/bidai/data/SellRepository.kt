package com.samrudha.bidai.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.samrudha.bidai.data.remote.ApiError
import com.samrudha.bidai.data.remote.CategoriesResponse
import com.samrudha.bidai.data.remote.CategoryDto
import com.samrudha.bidai.data.remote.CreateProductRequest
import com.samrudha.bidai.data.remote.CreateProductResponse
import com.samrudha.bidai.data.remote.SellApi
import com.samrudha.bidai.data.remote.UploadsResponse
import com.samrudha.bidai.ui.data.SampleHomeData
import com.samrudha.bidai.ui.models.CategoryUiModel
import java.io.IOException
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class SellRepository(
    private val api: SellApi,
    private val appContext: Context
) {

    suspend fun getCategories(): Result<List<CategoryUiModel>> =
        runCatchingResult {
            val response = api.getCategories()
            mapCategories(response)
        }

    suspend fun uploadImages(uris: List<Uri>): Result<List<String>> =
        runCatchingResult {
            val parts = uris.mapNotNull { uriToPart(it) }
            if (parts.isEmpty()) {
                throw Exception("Could not read selected photos")
            }
            api.uploadImages(parts).urls
        }

    suspend fun createProduct(request: CreateProductRequest): Result<CreateProductResponse> =
        runCatchingResult { api.createProduct(request) }

    private fun mapCategories(response: CategoriesResponse): List<CategoryUiModel> {
        return response.categories.map { dto ->
            val meta = slugMeta[dto.slug]
            CategoryUiModel(
                id = dto.slug,
                label = meta?.first ?: dto.name,
                emoji = meta?.second ?: "📦",
                iconRes = slugIcons[dto.slug],
                accentColor = meta?.third ?: 0xFF0668E1,
                apiId = dto.id
            )
        }
    }

    private fun fallbackCategories(): List<CategoryUiModel> {
        val apiIds = mapOf(
            "cars" to 1,
            "real_estate" to 2,
            "mobiles" to 3,
            "jobs" to 4,
            "bikes" to 5,
            "electronics" to 6,
            "home_garden" to 7,
            "beauty" to 8,
            "clothing" to 9,
            "books" to 10,
            "arts" to 11,
            "services" to 12,
            "general" to 13,
            "yodha" to 14,
            "agriculture" to 15
        )
        return SampleHomeData.categories.map { category ->
            category.copy(apiId = apiIds[category.id])
        }
    }

    fun fallbackCategoriesResult(): List<CategoryUiModel> = fallbackCategories()

    private fun uriToPart(uri: Uri): MultipartBody.Part? {
        return try {
            val inputStream = appContext.contentResolver.openInputStream(uri)
                ?: return null
            val bytes = inputStream.use { it.readBytes() }
            val mime = appContext.contentResolver.getType(uri)
                ?: "image/jpeg"
            val safeMime = if (mime.startsWith("image/")) mime else "image/jpeg"
            val extension = when {
                safeMime.contains("png") -> "png"
                safeMime.contains("webp") -> "webp"
                else -> "jpg"
            }
            val fileName = queryDisplayName(uri) ?: "photo_${System.nanoTime()}.$extension"
            val body = bytes.toRequestBody(safeMime.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", fileName, body)
        } catch (_: Exception) {
            null
        }
    }

    private fun queryDisplayName(uri: Uri): String? {
        return try {
            appContext.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) cursor.getString(index) else null
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun <T> runCatchingResult(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: retrofit2.HttpException) {
            Result.failure(Exception(parseError(e) ?: "Something went wrong, please try again"))
        } catch (_: SerializationException) {
            Result.failure(Exception("Something went wrong, please try again"))
        } catch (_: IOException) {
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

    private companion object {
        val slugMeta: Map<String, Triple<String, String, Long>> = mapOf(
            "cars" to Triple("Cars", "🚗", 0xFF0668E1),
            "real-estate" to Triple("Real Estate", "🏠", 0xFF00B894),
            "mobiles" to Triple("Mobiles", "📱", 0xFF6C5CE7),
            "jobs" to Triple("Jobs", "💼", 0xFFE17055),
            "bikes" to Triple("Bikes", "🏍️", 0xFF0984E3),
            "electronics" to Triple("Electronics", "🖥️", 0xFFD63031),
            "home-garden" to Triple("Home & Garden", "🛋️", 0xFF2D3436),
            "beauty" to Triple("Beauty", "👠", 0xFFE84393),
            "clothing" to Triple("Clothing", "👕", 0xFFFD79A8),
            "books" to Triple("Books", "📚", 0xFFA29BFE),
            "arts-and-crafts" to Triple("Arts and Crafts", "🎨", 0xFF55A3FF),
            "services" to Triple("Services", "🔧", 0xFF00CEC9),
            "general-service" to Triple("General Service", "⚙️", 0xFFFDCB6E),
            "yodha" to Triple("Yodha", "🪖", 0xFF636E72),
            "agriculture" to Triple("Agriculture", "🚜", 0xFF27AE60)
        )

        val slugIcons: Map<String, Int> = mapOf(
            "cars" to com.samrudha.bidai.R.drawable.cat_cars,
            "real-estate" to com.samrudha.bidai.R.drawable.cat_real_estate,
            "mobiles" to com.samrudha.bidai.R.drawable.cat_mobiles,
            "jobs" to com.samrudha.bidai.R.drawable.cat_jobs,
            "bikes" to com.samrudha.bidai.R.drawable.cat_bikes,
            "electronics" to com.samrudha.bidai.R.drawable.cat_electronics,
            "home-garden" to com.samrudha.bidai.R.drawable.cat_home_garden,
            "beauty" to com.samrudha.bidai.R.drawable.cat_beauty,
            "clothing" to com.samrudha.bidai.R.drawable.cat_clothing,
            "books" to com.samrudha.bidai.R.drawable.cat_books,
            "arts-and-crafts" to com.samrudha.bidai.R.drawable.cat_arts,
            "services" to com.samrudha.bidai.R.drawable.cat_services,
            "general-service" to com.samrudha.bidai.R.drawable.cat_general,
            "yodha" to com.samrudha.bidai.R.drawable.cat_yodha,
            "agriculture" to com.samrudha.bidai.R.drawable.cat_agriculture
        )
    }
}
