package com.samrudha.bidai.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samrudha.bidai.data.SellRepository
import com.samrudha.bidai.data.remote.CreateProductRequest
import com.samrudha.bidai.ui.models.CategoryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SellUiState(
    val categories: List<CategoryUiModel> = emptyList(),
    val selectedCategoryId: String = "bikes",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val showSuccess: Boolean = false
)

class SellViewModel(
    private val repository: SellRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellUiState())
    val uiState: StateFlow<SellUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(
                            categories = categories,
                            selectedCategoryId = categories.firstOrNull { c ->
                                c.id == it.selectedCategoryId
                            }?.id ?: categories.firstOrNull()?.id ?: it.selectedCategoryId
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(categories = repository.fallbackCategoriesResult())
                    }
                }
        }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun submit(
        photos: List<Uri>,
        title: String,
        description: String,
        brand: String,
        productType: String,
        location: String,
        price: String,
        sellAsBusiness: Boolean
    ) {
        if (_uiState.value.isSubmitting) return

        val titleTrimmed = title.trim()
        val priceValue = price.trim().toDoubleOrNull()
        val category = _uiState.value.categories.find {
            it.id == _uiState.value.selectedCategoryId
        }

        when {
            titleTrimmed.isEmpty() -> {
                _uiState.update { it.copy(error = "Ad Title is required") }
                return
            }
            priceValue == null || priceValue < 0 -> {
                _uiState.update { it.copy(error = "Enter a valid price") }
                return
            }
            photos.size < 2 -> {
                _uiState.update { it.copy(error = "Minimum 2 photos required") }
                return
            }
            photos.size > 5 -> {
                _uiState.update { it.copy(error = "Maximum 5 photos allowed") }
                return
            }
            category == null -> {
                _uiState.update { it.copy(error = "Select a category") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }

            repository.uploadImages(photos)
                .onSuccess { urls ->
                    val request = CreateProductRequest(
                        title = titleTrimmed,
                        description = description.trim(),
                        brand = brand.trim(),
                        productType = productType.trim(),
                        location = location.trim(),
                        sellAs = if (sellAsBusiness) "vendor" else "individual",
                        price = priceValue,
                        stock = 1,
                        categoryId = category.apiId,
                        imageUrls = urls
                    )
                    repository.createProduct(request)
                        .onSuccess {
                            _uiState.update {
                                it.copy(isSubmitting = false, showSuccess = true)
                            }
                        }
                        .onFailure { e ->
                            _uiState.update {
                                it.copy(
                                    isSubmitting = false,
                                    error = e.message ?: "Something went wrong, please try again"
                                )
                            }
                        }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            error = e.message ?: "Something went wrong, please try again"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(showSuccess = false) }
    }

    companion object {
        fun factory(repository: SellRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SellViewModel(repository)
                }
            }
    }
}
