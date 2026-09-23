package com.samrudha.bidai.ui.models

import androidx.annotation.DrawableRes

data class ProductUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: String,
    val originalPrice: String? = null,
    val ecoScore: String? = null,
    val isVerified: Boolean = false,
    val location: String,
    val date: String,
    val imageUrl: String? = null,
    @DrawableRes val imageRes: Int,
    val isFavorite: Boolean = false
)

data class CategoryUiModel(
    val id: String,
    val label: String,
    val emoji: String,
    @DrawableRes val iconRes: Int? = null,
    val accentColor: Long,
    val apiId: Int? = null
)

data class BannerUiModel(
    val id: String,
    val title: String? = null,
    val imageUrl: String? = null,
    @DrawableRes val imageRes: Int? = null,
    val cta: String? = null
)

data class TestimonialUiModel(
    val id: String,
    val quote: String,
    val name: String,
    val city: String,
    @DrawableRes val avatarRes: Int? = null
)

data class FeatureUiModel(
    val id: String,
    val body: String,
    @DrawableRes val imageRes: Int
)
