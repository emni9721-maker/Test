package app.zero.vault.data

import app.zero.vault.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class VaultRepository private constructor() {

    private val _currentUser = MutableStateFlow(MockDataProvider.CURRENT_USER)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _themeMode = MutableStateFlow(AppThemeMode.MIDNIGHT)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _photos = MutableStateFlow(MockDataProvider.INITIAL_PHOTOS)
    val photos: StateFlow<List<PhotoItem>> = _photos.asStateFlow()

    private val _albums = MutableStateFlow(MockDataProvider.INITIAL_ALBUMS)
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()

    private val _posts = MutableStateFlow(MockDataProvider.INITIAL_POSTS)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow(MockDataProvider.INITIAL_STORIES)
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _conversations = MutableStateFlow(MockDataProvider.INITIAL_CONVERSATIONS)
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _notifications = MutableStateFlow(MockDataProvider.INITIAL_NOTIFICATIONS)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _unlockedAlbumIds = MutableStateFlow<Set<String>>(emptySet())
    val unlockedAlbumIds: StateFlow<Set<String>> = _unlockedAlbumIds.asStateFlow()

    fun toggleTheme() {
        val next = if (_themeMode.value == AppThemeMode.MIDNIGHT) AppThemeMode.AMOLED else AppThemeMode.MIDNIGHT
        _themeMode.value = next
        _currentUser.value = _currentUser.value.copy(theme = next)
    }

    fun setTheme(mode: AppThemeMode) {
        _themeMode.value = mode
        _currentUser.value = _currentUser.value.copy(theme = mode)
    }

    fun toggleFavorite(photoId: String) {
        _photos.value = _photos.value.map { item ->
            if (item.id == photoId) item.copy(isFavorite = !item.isFavorite) else item
        }
    }

    fun deletePhoto(photoId: String) {
        _photos.value = _photos.value.map { item ->
            if (item.id == photoId) item.copy(isDeleted = true, deletedAt = "Just now") else item
        }
    }

    fun restorePhoto(photoId: String) {
        _photos.value = _photos.value.map { item ->
            if (item.id == photoId) item.copy(isDeleted = false, deletedAt = null) else item
        }
    }

    fun permanentlyDeletePhoto(photoId: String) {
        val target = _photos.value.find { it.id == photoId }
        _photos.value = _photos.value.filterNot { it.id == photoId }
        target?.let {
            _currentUser.value = _currentUser.value.copy(
                storageUsedBytes = maxOf(0L, _currentUser.value.storageUsedBytes - it.sizeBytes)
            )
        }
    }

    fun addPhoto(
        url: String,
        filename: String,
        caption: String,
        type: MediaType = MediaType.IMAGE,
        tags: List<String> = emptyList(),
        albumId: String? = null
    ) {
        val newSize = if (type == MediaType.VIDEO) 65_000_000L else 7_500_000L
        val newPhoto = PhotoItem(
            id = "photo_${UUID.randomUUID()}",
            userId = _currentUser.value.id,
            url = url,
            type = type,
            filename = filename,
            date = "Just now",
            caption = caption,
            sizeBytes = newSize,
            isFavorite = false,
            albumIds = if (albumId != null) listOf(albumId) else listOf("alb_travel"),
            tags = tags
        )
        _photos.value = listOf(newPhoto) + _photos.value
        _currentUser.value = _currentUser.value.copy(
            storageUsedBytes = _currentUser.value.storageUsedBytes + newSize
        )

        // Increment album count if assigned
        if (albumId != null) {
            _albums.value = _albums.value.map { alb ->
                if (alb.id == albumId) alb.copy(photoCount = alb.photoCount + 1) else alb
            }
        }
    }

    fun createAlbum(name: String, isPrivate: Boolean, isVaultLocked: Boolean, pin: String = "1234") {
        val newAlbum = Album(
            id = "alb_${UUID.randomUUID()}",
            userId = _currentUser.value.id,
            name = name,
            coverUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80",
            isPrivate = isPrivate,
            isVaultLocked = isVaultLocked,
            pinCode = pin,
            photoCount = 0,
            description = if (isVaultLocked) "Encrypted Vault Folder" else "Custom Album",
            createdAt = "Just now"
        )
        _albums.value = _albums.value + newAlbum
    }

    fun unlockAlbumWithPin(albumId: String, enteredPin: String): Boolean {
        val album = _albums.value.find { it.id == albumId } ?: return false
        if (album.pinCode == enteredPin || _currentUser.value.vaultPin == enteredPin) {
            _unlockedAlbumIds.value = _unlockedAlbumIds.value + albumId
            return true
        }
        return false
    }

    fun createPost(content: String, mediaUrl: String? = null, privacy: PostPrivacy = PostPrivacy.PUBLIC) {
        val newPost = Post(
            id = "post_${UUID.randomUUID()}",
            userId = _currentUser.value.id,
            user = _currentUser.value.toPreview(),
            content = content,
            mediaUrl = mediaUrl,
            privacy = privacy,
            createdAt = "Just now",
            likes = emptyList(),
            comments = emptyList(),
            sharesCount = 0
        )
        _posts.value = listOf(newPost) + _posts.value
    }

    fun toggleLikePost(postId: String) {
        val currentUserId = _currentUser.value.id
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newLikes = if (post.likes.contains(currentUserId)) {
                    post.likes - currentUserId
                } else {
                    post.likes + currentUserId
                }
                post.copy(likes = newLikes)
            } else post
        }
    }

    fun addComment(postId: String, text: String) {
        val newComment = Comment(
            id = "c_${UUID.randomUUID()}",
            userId = _currentUser.value.id,
            user = _currentUser.value.toPreview(),
            text = text,
            createdAt = "Just now"
        )
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(comments = post.comments + newComment)
            } else post
        }
    }

    fun sendMessage(receiverId: String, text: String) {
        val myId = _currentUser.value.id
        val newMsg = Message(
            id = "m_${UUID.randomUUID()}",
            senderId = myId,
            receiverId = receiverId,
            text = text,
            createdAt = "Just now",
            isRead = true
        )

        val existingConv = _conversations.value.find { it.otherUser.id == receiverId }
        if (existingConv != null) {
            _conversations.value = _conversations.value.map { conv ->
                if (conv.otherUser.id == receiverId) {
                    conv.copy(lastMessage = newMsg)
                } else conv
            }
        } else {
            val otherUser = MockDataProvider.OTHER_USERS.find { it.id == receiverId } ?: UserPreview(
                id = receiverId,
                username = "contact",
                displayName = "Contact",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80"
            )
            _conversations.value = listOf(
                Conversation(
                    otherUser = otherUser,
                    lastMessage = newMsg,
                    unreadCount = 0
                )
            ) + _conversations.value
        }
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun updateVaultPin(newPin: String) {
        _currentUser.value = _currentUser.value.copy(vaultPin = newPin)
    }

    companion object {
        val instance by lazy { VaultRepository() }
    }
}
