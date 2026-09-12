package app.zero.vault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

enum class NavigationTab {
    FEED,
    PHOTOS,
    ALBUMS,
    NOTIFICATIONS,
    PROFILE
}

@Composable
fun ZeroBottomNavBar(
    currentTab: NavigationTab,
    unreadNotificationsCount: Int,
    onTabSelected: (NavigationTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.testTag("zero_bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 6.dp
    ) {
        // 1. Feed
        NavigationBarItem(
            modifier = Modifier.testTag("nav_tab_feed"),
            selected = currentTab == NavigationTab.FEED,
            onClick = { onTabSelected(NavigationTab.FEED) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.FEED) Icons.Filled.DynamicFeed else Icons.Outlined.DynamicFeed,
                    contentDescription = "Social & Vault Feed"
                )
            },
            label = { Text("Feed") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 2. Photos Gallery
        NavigationBarItem(
            modifier = Modifier.testTag("nav_tab_photos"),
            selected = currentTab == NavigationTab.PHOTOS,
            onClick = { onTabSelected(NavigationTab.PHOTOS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PHOTOS) Icons.Filled.PhotoLibrary else Icons.Outlined.PhotoLibrary,
                    contentDescription = "Photos & Media"
                )
            },
            label = { Text("Photos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 3. Vault & Albums
        NavigationBarItem(
            modifier = Modifier.testTag("nav_tab_albums"),
            selected = currentTab == NavigationTab.ALBUMS,
            onClick = { onTabSelected(NavigationTab.ALBUMS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.ALBUMS) Icons.Filled.Lock else Icons.Outlined.Lock,
                    contentDescription = "Vault Albums"
                )
            },
            label = { Text("Vault") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 4. Activity / Notifications
        NavigationBarItem(
            modifier = Modifier.testTag("nav_tab_notifications"),
            selected = currentTab == NavigationTab.NOTIFICATIONS,
            onClick = { onTabSelected(NavigationTab.NOTIFICATIONS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotificationsCount > 0) {
                            Badge { Text(unreadNotificationsCount.toString()) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == NavigationTab.NOTIFICATIONS) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Activity Notifications"
                    )
                }
            },
            label = { Text("Activity") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 5. User Profile & Settings
        NavigationBarItem(
            modifier = Modifier.testTag("nav_tab_profile"),
            selected = currentTab == NavigationTab.PROFILE,
            onClick = { onTabSelected(NavigationTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PROFILE) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = "My Profile & Vault"
                )
            },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
