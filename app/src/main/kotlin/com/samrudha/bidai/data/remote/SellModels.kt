package com.samrudha.bidai.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val slug: String
)

@Serializable
data class CategoriesResponse(
    val categories: List<CategoryDto>
)

@Serializable
data class UploadsResponse(
    val urls: List<String>
)

@Serializable
data class CreateProductRequest(
    val title: String,
    val description: String = "",
    val brand: String = "",
    @SerialName("product_type") val productType: String = "",
    val location: String = "",
    @SerialName("sell_as") val sellAs: String,
    val price: Double,
    val stock: Int = 1,
    @SerialName("category_id") val categoryId: Int? = null,
    @SerialName("image_urls") val imageUrls: List<String>
)

@Serializable
data class ProductDto(
    val id: Int,
    val title: String? = null,
    val price: Double? = null,
    val status: String? = null,
    val images: List<String>? = null
)

@Serializable
data class CreateProductResponse(
    val product: ProductDto
)
