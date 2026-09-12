package app.zero.vault.model

enum class MediaType {
    IMAGE,
    VIDEO,
    DOCUMENT,
    AUDIO,
    RAW
}

enum class PostPrivacy {
    PUBLIC,
    FRIENDS,
    PRIVATE
}

enum class StoragePlanTier(val label: String, val limitBytes: Long) {
    STARTER_50GB("Starter (50 GB)", 50L * 1024 * 1024 * 1024),
    PRO_2TB("Pro Vault (2 TB)", 2048L * 1024 * 1024 * 1024),
    STUDIO_10TB("Studio (10 TB)", 10240L * 1024 * 1024 * 1024),
    INFINITE_UNLIMITED("Infinite Vault", Long.MAX_VALUE)
}

enum class AppThemeMode {
    MIDNIGHT,
    AMOLED
}

data class UserPreview(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val isVerified: Boolean = false
)

data class StorageSettings(
    val backupOverWifiOnly: Boolean = false,
    val autoCompress: Boolean = false,
    val qualityPreset: String = "high",
    val autoBackupEnabled: Boolean = true,
    val autoBackupInterval: String = "realtime",
    val backupOriginalMedia: Boolean = true
)

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val coverUrl: String,
    val bio: String,
    val followersCount: Int,
    val followingCount: Int,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val storageUsedBytes: Long,
    val storageLimitBytes: Long,
    val storagePlan: StoragePlanTier = StoragePlanTier.PRO_2TB,
    val storageSettings: StorageSettings = StorageSettings(),
    val isPrivate: Boolean = false,
    val vaultPin: String = "1234",
    val theme: AppThemeMode = AppThemeMode.MIDNIGHT
) {
    fun toPreview(): UserPreview = UserPreview(
        id = id,
        username = username,
        displayName = displayName,
        avatarUrl = avatarUrl,
        isVerified = true
    )
}

data class PhotoItem(
    val id: String,
    val userId: String,
    val url: String,
    val type: MediaType = MediaType.IMAGE,
    val filename: String,
    val date: String,
    val caption: String = "",
    val sizeBytes: Long,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedAt: String? = null,
    val albumIds: List<String> = emptyList(),
    val duration: String? = null,
    val location: String? = null,
    val tags: List<String> = emptyList()
)

data class Album(
    val id: String,
    val userId: String,
    val name: String,
    val coverUrl: String,
    val isPrivate: Boolean = false,
    val isVaultLocked: Boolean = false,
    val pinCode: String = "1234",
    val description: String = "",
    val createdAt: String = "",
    val photoCount: Int = 0
)

data class Comment(
    val id: String,
    val userId: String,
    val user: UserPreview,
    val text: String,
    val createdAt: String,
    val likesCount: Int = 0
)

data class Post(
    val id: String,
    val userId: String,
    val user: UserPreview,
    val content: String,
    val mediaUrl: String? = null,
    val mediaType: MediaType = MediaType.IMAGE,
    val privacy: PostPrivacy = PostPrivacy.PUBLIC,
    val createdAt: String,
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val sharesCount: Int = 0,
    val savedBy: List<String> = emptyList()
)

data class Story(
    val id: String,
    val userId: String,
    val user: UserPreview,
    val mediaUrl: String,
    val mediaType: MediaType = MediaType.IMAGE,
    val caption: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val viewersCount: Int = 12,
    val isLiked: Boolean = false
)

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val mediaUrl: String? = null,
    val createdAt: String,
    val isRead: Boolean = true
)

data class Conversation(
    val otherUser: UserPreview,
    val lastMessage: Message,
    val unreadCount: Int = 0
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    FRIEND_REQUEST,
    STORAGE_ALERT,
    BACKUP_COMPLETED
}

data class NotificationItem(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val actor: UserPreview,
    val message: String,
    val createdAt: String,
    val isRead: Boolean = false
)
