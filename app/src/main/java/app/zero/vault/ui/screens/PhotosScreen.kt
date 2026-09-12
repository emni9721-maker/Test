package app.zero.vault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.zero.vault.model.*
import coil.compose.AsyncImage

enum class PhotosFilterTab {
    ALL,
    FAVORITES,
    TRASH
}

@Composable
fun PhotosScreen(
    photos: List<PhotoItem>,
    storageUsedBytes: Long,
    storageLimitBytes: Long,
    onPhotoClick: (PhotoItem) -> Unit,
    onUploadClick: () -> Unit
) {
    var activeTab by remember { mutableStateOf(PhotosFilterTab.ALL) }

    val filteredPhotos = remember(photos, activeTab) {
        when (activeTab) {
            PhotosFilterTab.ALL -> photos.filter { !it.isDeleted }
            PhotosFilterTab.FAVORITES -> photos.filter { !it.isDeleted && it.isFavorite }
            PhotosFilterTab.TRASH -> photos.filter { it.isDeleted }
        }
    }

    val usedGb = String.format("%.1f", storageUsedBytes.toDouble() / (1024 * 1024 * 1024))
    val totalGb = String.format("%.0f", storageLimitBytes.toDouble() / (1024 * 1024 * 1024))

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onUploadClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("photos_upload_fab")
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = "Upload Photo or Video")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("photos_gallery_screen")
        ) {
            // Storage Quota Bar Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cloud Vault Storage",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$usedGb GB / $totalGb GB (2 TB Pro)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (storageUsedBytes.toFloat() / storageLimitBytes.toFloat()).coerceIn(0.01f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🔒 Zero-Knowledge AES-256 Enabled",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Wi-Fi Realtime Sync",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Filter Tabs (All / Favorites / Trash)
            TabRow(
                selectedTabIndex = activeTab.ordinal,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("photos_filter_tabs")
            ) {
                Tab(
                    selected = activeTab == PhotosFilterTab.ALL,
                    onClick = { activeTab = PhotosFilterTab.ALL },
                    text = { Text("All Media (${photos.count { !it.isDeleted }})") }
                )
                Tab(
                    selected = activeTab == PhotosFilterTab.FAVORITES,
                    onClick = { activeTab = PhotosFilterTab.FAVORITES },
                    text = { Text("Favorites (${photos.count { !it.isDeleted && it.isFavorite }})") }
                )
                Tab(
                    selected = activeTab == PhotosFilterTab.TRASH,
                    onClick = { activeTab = PhotosFilterTab.TRASH },
                    text = { Text("Trash (${photos.count { it.isDeleted }})") }
                )
            }

            // Media Grid
            if (filteredPhotos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (activeTab == PhotosFilterTab.TRASH) "Trash is empty" else "No photos found",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .testTag("photos_grid"),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredPhotos, key = { it.id }) { photo ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onPhotoClick(photo) }
                                .testTag("photo_item_${photo.id}")
                        ) {
                            AsyncImage(
                                model = photo.url,
                                contentDescription = photo.caption,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Favorite Icon Badge
                            if (photo.isFavorite) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Starred",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(16.dp)
                                )
                            }

                            // Video Duration Tag
                            if (photo.type == MediaType.VIDEO) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = photo.duration ?: "VIDEO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
