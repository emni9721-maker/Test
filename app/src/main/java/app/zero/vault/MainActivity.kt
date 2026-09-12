package app.zero.vault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import app.zero.vault.data.VaultRepository
import app.zero.vault.model.*
import app.zero.vault.ui.components.*
import app.zero.vault.ui.screens.*
import app.zero.vault.ui.theme.ZeroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = VaultRepository.instance

        setContent {
            val themeMode by repository.themeMode.collectAsState()
            val currentUser by repository.currentUser.collectAsState()
            val photos by repository.photos.collectAsState()
            val albums by repository.albums.collectAsState()
            val posts by repository.posts.collectAsState()
            val stories by repository.stories.collectAsState()
            val conversations by repository.conversations.collectAsState()
            val notifications by repository.notifications.collectAsState()
            val unlockedAlbumIds by repository.unlockedAlbumIds.collectAsState()

            var currentTab by remember { mutableStateOf(NavigationTab.FEED) }
            var showingSettings by remember { mutableStateOf(false) }

            // Active Modals & Dialogs State
            var viewingStory by remember { mutableStateOf<Story?>(null) }
            var viewingPhoto by remember { mutableStateOf<PhotoItem?>(null) }
            var showUploadDialog by remember { mutableStateOf(false) }
            var showCreatePostDialog by remember { mutableStateOf(false) }
            var showMessengerDialog by remember { mutableStateOf(false) }
            var pinDialogAlbum by remember { mutableStateOf<Album?>(null) }

            val unreadNotifsCount = notifications.count { !it.isRead }
            val unreadMessagesCount = conversations.sumOf { it.unreadCount }

            val usedGb = String.format("%.1f", currentUser.storageUsedBytes.toDouble() / (1024 * 1024 * 1024))
            val totalGb = String.format("%.0f", currentUser.storageLimitBytes.toDouble() / (1024 * 1024 * 1024))
            val storageStatusText = "$usedGb GB / $totalGb GB"

            ZeroTheme(themeMode = themeMode) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("app_root_scaffold"),
                    topBar = {
                        if (!showingSettings) {
                            ZeroTopAppBar(
                                themeMode = themeMode,
                                unreadMessagesCount = unreadMessagesCount,
                                storageUsedText = storageStatusText,
                                onToggleTheme = { repository.toggleTheme() },
                                onOpenMessenger = { showMessengerDialog = true },
                                onOpenStorageDetails = { showingSettings = true }
                            )
                        }
                    },
                    bottomBar = {
                        if (!showingSettings) {
                            ZeroBottomNavBar(
                                currentTab = currentTab,
                                unreadNotificationsCount = unreadNotifsCount,
                                onTabSelected = { tab ->
                                    currentTab = tab
                                }
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showingSettings) {
                            SettingsScreen(
                                user = currentUser,
                                themeMode = themeMode,
                                onBack = { showingSettings = false },
                                onThemeChange = { mode -> repository.setTheme(mode) },
                                onUpdatePin = { pin -> repository.updateVaultPin(pin) }
                            )
                        } else {
                            when (currentTab) {
                                NavigationTab.FEED -> {
                                    HomeScreen(
                                        posts = posts,
                                        stories = stories,
                                        currentUserId = currentUser.id,
                                        onStoryClick = { story -> viewingStory = story },
                                        onLikePost = { postId -> repository.toggleLikePost(postId) },
                                        onAddComment = { postId, text -> repository.addComment(postId, text) },
                                        onCreatePostClick = { showCreatePostDialog = true },
                                        onUploadMediaClick = { showUploadDialog = true }
                                    )
                                }
                                NavigationTab.PHOTOS -> {
                                    PhotosScreen(
                                        photos = photos,
                                        storageUsedBytes = currentUser.storageUsedBytes,
                                        storageLimitBytes = currentUser.storageLimitBytes,
                                        onPhotoClick = { photo -> viewingPhoto = photo },
                                        onUploadClick = { showUploadDialog = true }
                                    )
                                }
                                NavigationTab.ALBUMS -> {
                                    AlbumsScreen(
                                        albums = albums,
                                        unlockedAlbumIds = unlockedAlbumIds,
                                        onAlbumClick = { album ->
                                            if (album.isVaultLocked && !unlockedAlbumIds.contains(album.id)) {
                                                pinDialogAlbum = album
                                            } else {
                                                // Switch to photos tab to view album items
                                                currentTab = NavigationTab.PHOTOS
                                            }
                                        },
                                        onCreateAlbum = { name, isPriv, isVault ->
                                            repository.createAlbum(name, isPriv, isVault)
                                        }
                                    )
                                }
                                NavigationTab.NOTIFICATIONS -> {
                                    NotificationsScreen(
                                        notifications = notifications,
                                        onMarkAllRead = { repository.markAllNotificationsRead() }
                                    )
                                }
                                NavigationTab.PROFILE -> {
                                    ProfileScreen(
                                        user = currentUser,
                                        userPhotos = photos.filter { it.userId == currentUser.id && !it.isDeleted },
                                        userPosts = posts.filter { it.userId == currentUser.id },
                                        onOpenSettings = { showingSettings = true },
                                        onPhotoClick = { photo -> viewingPhoto = photo }
                                    )
                                }
                            }
                        }
                    }

                    // Story Viewer Modal
                    viewingStory?.let { story ->
                        StoryViewerModal(
                            story = story,
                            onDismiss = { viewingStory = null }
                        )
                    }

                    // Photo Detail Modal
                    viewingPhoto?.let { photo ->
                        PhotoDetailModal(
                            photo = photo,
                            onDismiss = { viewingPhoto = null },
                            onToggleFavorite = { photoId -> repository.toggleFavorite(photoId) },
                            onDeletePhoto = { photoId -> repository.deletePhoto(photoId) },
                            onRestorePhoto = { photoId -> repository.restorePhoto(photoId) }
                        )
                    }

                    // Upload Dialog
                    if (showUploadDialog) {
                        UploadMediaDialog(
                            albums = albums,
                            onDismiss = { showUploadDialog = false },
                            onUploadConfirmed = { url, filename, caption, type, albumId ->
                                repository.addPhoto(url, filename, caption, type, albumId = albumId)
                                showUploadDialog = false
                            }
                        )
                    }

                    // Create Post Dialog
                    if (showCreatePostDialog) {
                        CreatePostDialog(
                            onDismiss = { showCreatePostDialog = false },
                            onPostCreated = { content, mediaUrl, privacy ->
                                repository.createPost(content, mediaUrl, privacy)
                                showCreatePostDialog = false
                            }
                        )
                    }

                    // Messenger Dialog
                    if (showMessengerDialog) {
                        MessengerDialog(
                            conversations = conversations,
                            onDismiss = { showMessengerDialog = false },
                            onSendMessage = { receiverId, text ->
                                repository.sendMessage(receiverId, text)
                            }
                        )
                    }

                    // Vault PIN Unlock Dialog
                    pinDialogAlbum?.let { album ->
                        VaultPinDialog(
                            albumName = album.name,
                            onDismiss = { pinDialogAlbum = null },
                            onPinEntered = { pin ->
                                val success = repository.unlockAlbumWithPin(album.id, pin)
                                if (success) {
                                    pinDialogAlbum = null
                                    currentTab = NavigationTab.PHOTOS
                                }
                                success
                            }
                        )
                    }
                }
            }
        }
    }
}
