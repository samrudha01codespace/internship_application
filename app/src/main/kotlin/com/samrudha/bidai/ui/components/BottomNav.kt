package com.samrudha.bidai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samrudha.bidai.R

data class BottomNavItem(
    val route: String,
    val labelRes: Int,
    val iconRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem("home", R.string.nav_home, R.drawable.ic_nav_home),
    BottomNavItem("sell", R.string.nav_sell, R.drawable.ic_nav_sell),
    BottomNavItem("one_click", R.string.nav_one_click, R.drawable.ic_nav_one_click),
    BottomNavItem("chat", R.string.nav_chat, R.drawable.ic_nav_chat),
    BottomNavItem("your_items", R.string.nav_your_items, R.drawable.ic_nav_your_items)
)

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) onNavigate(item.route)
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(item.iconRes),
                            contentDescription = stringResource(item.labelRes),
                            modifier = Modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(
                                if (selected) MaterialTheme.colorScheme.primary
                                else Color(0xFF9CA3AF)
                            )
                        )
                        if (selected) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else Color(0xFF9CA3AF)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF),
                    indicatorColor = Color(0xFFD6E4FF)
                ),
                alwaysShowLabel = true
            )
        }
    }
}
