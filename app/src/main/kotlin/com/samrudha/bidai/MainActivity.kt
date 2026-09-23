package com.samrudha.bidai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.samrudha.bidai.data.NetworkModule
import com.samrudha.bidai.data.SellRepository
import com.samrudha.bidai.data.TokenStore
import com.samrudha.bidai.ui.AuthViewModel
import com.samrudha.bidai.ui.HomeScreen
import com.samrudha.bidai.ui.HomeViewModel
import com.samrudha.bidai.ui.LoginScreen
import com.samrudha.bidai.ui.SellScreen
import com.samrudha.bidai.ui.SellViewModel
import com.samrudha.bidai.ui.SignupScreen
import com.samrudha.bidai.ui.TabPlaceholderScreen
import com.samrudha.bidai.ui.components.AppBottomBar
import com.samrudha.bidai.ui.theme.bidaiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenStore = TokenStore(this)
        NetworkModule.init(tokenStore)

        setContent {
            bidaiTheme {
                val navController = rememberNavController()
                val startDestination = if (tokenStore.hasToken()) "home" else "login"
                val authViewModel: AuthViewModel =
                    viewModel(factory = AuthViewModel.factory(tokenStore))
                val homeViewModel: HomeViewModel =
                    viewModel(factory = HomeViewModel.factory(tokenStore))
                val sellViewModel: SellViewModel =
                    viewModel(
                        factory = SellViewModel.factory(
                            SellRepository(NetworkModule.sellApi, applicationContext)
                        )
                    )

                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val bottomRoutes = setOf("home", "sell", "one_click", "chat", "your_items")
                val showBottomBar = currentRoute in bottomRoutes

                fun navigateToTab(route: String) {
                    navController.navigate(route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                Scaffold(
                    containerColor = Color.White,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        if (showBottomBar) {
                            AppBottomBar(
                                currentRoute = currentRoute,
                                onNavigate = ::navigateToTab
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable("login") {
                            val state by authViewModel.uiState.collectAsStateWithLifecycle()

                            LaunchedEffect(state.success) {
                                if (state.success) {
                                    authViewModel.consumeSuccess()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }

                            LoginScreen(
                                uiState = state,
                                onSignIn = { email, password ->
                                    authViewModel.login(email, password)
                                },
                                onSignUp = {
                                    authViewModel.clearError()
                                    navController.navigate("signup")
                                },
                                onErrorShown = { authViewModel.clearError() }
                            )
                        }

                        composable("signup") {
                            val state by authViewModel.uiState.collectAsStateWithLifecycle()

                            LaunchedEffect(state.success) {
                                if (state.success) {
                                    authViewModel.consumeSuccess()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }

                            SignupScreen(
                                uiState = state,
                                onSignUp = { name, mobile, email, password ->
                                    authViewModel.signup(name, mobile, email, password)
                                },
                                onSignIn = {
                                    authViewModel.clearError()
                                    navController.popBackStack()
                                },
                                onErrorShown = { authViewModel.clearError() }
                            )
                        }

                        composable("home") {
                            val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()

                            HomeScreen(
                                uiState = homeState,
                                onLocationClick = {},
                                onNotifications = {},
                                onFavorites = {},
                                onMenu = {},
                                onAvatar = {},
                                onCameraSearch = {},
                                onCategoryClick = {},
                                onProductClick = {},
                                onToggleFavorite = homeViewModel::toggleFavorite,
                                onRenewPlan = {},
                                onInvite = {},
                                onErrorShown = homeViewModel::clearError
                            )
                        }

                        composable("sell") {
                            val sellState by sellViewModel.uiState.collectAsStateWithLifecycle()

                            SellScreen(
                                uiState = sellState,
                                onBack = { navController.popBackStack() },
                                onCategoryClick = sellViewModel::selectCategory,
                                onUpload = { photos, title, description, brand, productType, location, price, sellAsBusiness ->
                                    sellViewModel.submit(
                                        photos = photos,
                                        title = title,
                                        description = description,
                                        brand = brand,
                                        productType = productType,
                                        location = location,
                                        price = price,
                                        sellAsBusiness = sellAsBusiness
                                    )
                                },
                                onErrorShown = sellViewModel::clearError,
                                onSuccessShown = sellViewModel::clearSuccess
                            )
                        }
                        composable("one_click") {
                            TabPlaceholderScreen(title = stringResource(R.string.nav_one_click))
                        }
                        composable("chat") {
                            TabPlaceholderScreen(title = stringResource(R.string.nav_chat))
                        }
                        composable("your_items") {
                            TabPlaceholderScreen(title = stringResource(R.string.nav_your_items))
                        }
                    }
                }
            }
        }
    }
}
