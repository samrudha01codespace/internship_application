package com.samrudha.bidai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samrudha.bidai.data.TokenStore
import com.samrudha.bidai.ui.data.SampleHomeData
import com.samrudha.bidai.ui.models.BannerUiModel
import com.samrudha.bidai.ui.models.CategoryUiModel
import com.samrudha.bidai.ui.models.FeatureUiModel
import com.samrudha.bidai.ui.models.ProductUiModel
import com.samrudha.bidai.ui.models.TestimonialUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val location: String = "Taj Retreat, Bengaluru",
    val searchHint: String = "Search For Cars",
    val carFilters: List<String> = emptyList(),
    val bikeFilters: List<String> = emptyList(),
    val categories: List<CategoryUiModel> = emptyList(),
    val heroBanners: List<BannerUiModel> = emptyList(),
    val promoBanners: List<BannerUiModel> = emptyList(),
    val cars: List<ProductUiModel> = emptyList(),
    val bikes: List<ProductUiModel> = emptyList(),
    val recommendations: List<ProductUiModel> = emptyList(),
    val features: List<FeatureUiModel> = emptyList(),
    val testimonials: List<TestimonialUiModel> = emptyList(),
    val favoriteIds: Set<String> = emptySet()
)

class HomeViewModel(
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    /**
     * Static sample load. Replace body with products API when ready:
     * repository.getProducts() → map DTO → ProductUiModel (keep imageUrl from API).
     */
    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // TODO: swap for NetworkModule.productsApi.getProducts() when integrating API
            _uiState.update {
                HomeUiState(
                    isLoading = false,
                    location = "Taj Retreat, Bengaluru",
                    carFilters = SampleHomeData.carFilters,
                    bikeFilters = SampleHomeData.bikeFilters,
                    categories = SampleHomeData.categories,
                    heroBanners = SampleHomeData.heroBanners,
                    promoBanners = SampleHomeData.promoBanners,
                    cars = SampleHomeData.cars,
                    bikes = SampleHomeData.bikes,
                    recommendations = SampleHomeData.recommendations,
                    features = SampleHomeData.features,
                    testimonials = SampleHomeData.testimonials,
                    favoriteIds = it.favoriteIds
                )
            }
        }
    }

    fun toggleFavorite(productId: String) {
        _uiState.update { state ->
            val next = state.favoriteIds.toMutableSet()
            if (!next.add(productId)) next.remove(productId)
            val cars = state.cars.map {
                if (it.id == productId) it.copy(isFavorite = productId in next) else it
            }
            val bikes = state.bikes.map {
                if (it.id == productId) it.copy(isFavorite = productId in next) else it
            }
            val recommendations = state.recommendations.map {
                if (it.id == productId) it.copy(isFavorite = productId in next) else it
            }
            state.copy(
                favoriteIds = next,
                cars = cars,
                bikes = bikes,
                recommendations = recommendations
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun logout() {
        tokenStore.clear()
    }

    companion object {
        fun factory(tokenStore: TokenStore): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(tokenStore)
            }
        }
    }
}
