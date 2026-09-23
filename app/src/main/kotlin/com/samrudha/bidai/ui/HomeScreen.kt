package com.samrudha.bidai.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samrudha.bidai.R
import com.samrudha.bidai.ui.components.CategoryItem
import com.samrudha.bidai.ui.components.FeatureRow
import com.samrudha.bidai.ui.components.FilterChipRow
import com.samrudha.bidai.ui.components.HomeSearchBar
import com.samrudha.bidai.ui.components.HomeTopBar
import com.samrudha.bidai.ui.components.InviteBanner
import com.samrudha.bidai.ui.components.PlanExpiryBanner
import com.samrudha.bidai.ui.components.ProductCard
import com.samrudha.bidai.ui.components.PromoBannerCarousel
import com.samrudha.bidai.ui.components.SectionHeader
import com.samrudha.bidai.ui.components.TestimonialCard
import com.samrudha.bidai.ui.data.SampleHomeData
import com.samrudha.bidai.ui.models.ProductUiModel
import com.samrudha.bidai.ui.theme.bidaiTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onLocationClick: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onFavorites: () -> Unit = {},
    onMenu: () -> Unit = {},
    onAvatar: () -> Unit = {},
    onCameraSearch: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onProductClick: (ProductUiModel) -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onRenewPlan: () -> Unit = {},
    onInvite: () -> Unit = {},
    onErrorShown: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val closeLabel = stringResource(R.string.error_close)
    var carFilterIndex by remember { mutableIntStateOf(0) }
    var bikeFilterIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = closeLabel,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            onErrorShown()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .systemBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HomeTopBar(
                    location = uiState.location,
                    logoClickLabel = stringResource(R.string.app_name),
                    onLocationClick = onLocationClick,
                    onNotifications = onNotifications,
                    onFavorites = onFavorites,
                    onGrid = onMenu,
                    onAvatar = onAvatar
                )
            }

            item {
                HomeSearchBar(
                    query = "",
                    hint = uiState.searchHint,
                    onCamera = onCameraSearch
                )
            }

            item {
                PromoBannerCarousel(
                    banners = uiState.heroBanners,
                    height = 140.dp
                )
            }

            item {
                Column {
                    SectionHeader(title = stringResource(R.string.home_explore_categories))
                    Spacer(modifier = Modifier.height(8.dp))
                    // 15 categories / 4 cols = 4 rows; item ≈ 96dp incl. label
                    val categoryRows = (uiState.categories.size + 3) / 4
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((categoryRows * 96 + 8).dp)
                            .padding(horizontal = 12.dp),
                        userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.categories, key = { it.id }) { category ->
                            CategoryItem(
                                category = category,
                                onClick = { onCategoryClick(category.id) }
                            )
                        }
                    }
                }
            }

            item {
                PromoBannerCarousel(
                    banners = uiState.promoBanners,
                    height = 150.dp
                )
            }

            item {
                ProductSection(
                    title = stringResource(R.string.home_newly_listed_cars),
                    filters = uiState.carFilters,
                    selectedFilter = carFilterIndex,
                    onFilterSelected = { carFilterIndex = it },
                    products = uiState.cars,
                    onProductClick = onProductClick,
                    onToggleFavorite = onToggleFavorite
                )
            }

            item {
                ProductSection(
                    title = stringResource(R.string.home_newly_listed_bikes),
                    filters = uiState.bikeFilters,
                    selectedFilter = bikeFilterIndex,
                    onFilterSelected = { bikeFilterIndex = it },
                    products = uiState.bikes,
                    onProductClick = onProductClick,
                    onToggleFavorite = onToggleFavorite
                )
            }

            item {
                PlanExpiryBanner(onRenew = onRenewPlan)
            }

            item {
                ProductSection(
                    title = stringResource(R.string.home_recommendations),
                    filters = emptyList(),
                    selectedFilter = 0,
                    onFilterSelected = {},
                    products = uiState.recommendations,
                    onProductClick = onProductClick,
                    onToggleFavorite = onToggleFavorite,
                    grid = true
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(title = stringResource(R.string.home_sell_buy_title))
                    FeatureRow(features = uiState.features)
                }
            }

            item {
                FeatureBannerSection()
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(title = stringResource(R.string.home_testimonials))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.testimonials, key = { it.id }) { item ->
                            TestimonialCard(testimonial = item)
                        }
                    }
                }
            }

            item {
                InviteBanner(onInvite = onInvite)
            }
        }
    }
}

@Composable
private fun ProductSection(
    title: String,
    filters: List<String>,
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit,
    products: List<ProductUiModel>,
    onProductClick: (ProductUiModel) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
    grid: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(title = title, actionLabel = "See all") {}

        if (filters.isNotEmpty()) {
            FilterChipRow(
                chips = filters,
                selectedIndex = selectedFilter,
                onSelected = onFilterSelected
            )
        }

        if (grid) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                products.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { product ->
                            ProductCard(
                                product = product,
                                modifier = Modifier.weight(1f),
                                onClick = { onProductClick(product) },
                                onFavorite = { onToggleFavorite(product.id) }
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        modifier = Modifier.width(168.dp),
                        onClick = { onProductClick(product) },
                        onFavorite = { onToggleFavorite(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureBannerSection() {
    Image(
        painter = painterResource(R.drawable.banner_property),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    bidaiTheme {
        HomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                carFilters = SampleHomeData.carFilters,
                bikeFilters = SampleHomeData.bikeFilters,
                categories = SampleHomeData.categories,
                heroBanners = SampleHomeData.heroBanners,
                promoBanners = SampleHomeData.promoBanners,
                cars = SampleHomeData.cars,
                bikes = SampleHomeData.bikes,
                recommendations = SampleHomeData.recommendations,
                features = SampleHomeData.features,
                testimonials = SampleHomeData.testimonials
            )
        )
    }
}
