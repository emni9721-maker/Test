package app.zero.vault.data

import app.zero.vault.model.*

object MockDataProvider {

    val CURRENT_USER = User(
        id = "u_zero_me",
        username = "kai.vance",
        displayName = "Kai Vance",
        email = "kai.vance@zero.io",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
        coverUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80",
        bio = "Visual designer & ambient explorer. Storing moments in ZERO vault. 🌿📸",
        followersCount = 842,
        followingCount = 319,
        followers = listOf("u_maya", "u_elena", "u_marcus"),
        following = listOf("u_maya", "u_elena"),
        friends = listOf("u_maya", "u_elena"),
        storageUsedBytes = 15891390464L, // 14.8 GB
        storageLimitBytes = 2199023255552L, // 2 TB
        storagePlan = StoragePlanTier.PRO_2TB,
        vaultPin = "1234",
        theme = AppThemeMode.MIDNIGHT
    )

    val OTHER_USERS = listOf(
        UserPreview(
            id = "u_maya",
            username = "maya.chen",
            displayName = "Maya Chen",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
            isVerified = true
        ),
        UserPreview(
            id = "u_elena",
            username = "elena_rostova",
            displayName = "Elena Rostova",
            avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=400&q=80",
            isVerified = true
        ),
        UserPreview(
            id = "u_marcus",
            username = "marcus.v",
            displayName = "Marcus Vance",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            isVerified = false
        )
    )

    val INITIAL_PHOTOS = listOf(
        PhotoItem(
            id = "photo_1",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.IMAGE,
            filename = "ZERO_IMG_20260908_YosemiteDawn.jpg",
            date = "2026-09-08T06:14:22Z",
            caption = "Early morning mist clearing over Yosemite valley.",
            sizeBytes = 8450120L,
            isFavorite = true,
            albumIds = listOf("alb_travel", "alb_favorites"),
            location = "Yosemite National Park, CA",
            tags = listOf("Landscape", "Nature", "Fog")
        ),
        PhotoItem(
            id = "photo_2",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.IMAGE,
            filename = "ZERO_IMG_20260906_StarryPeak.jpg",
            date = "2026-09-06T22:45:10Z",
            caption = "Alpine peak under the Milky Way canopy.",
            sizeBytes = 14200000L,
            isFavorite = true,
            albumIds = listOf("alb_travel", "alb_favorites"),
            location = "Swiss Alps",
            tags = listOf("Astro", "Night", "Stars")
        ),
        PhotoItem(
            id = "photo_3",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.IMAGE,
            filename = "ZERO_IMG_20260901_NordicForest.jpg",
            date = "2026-09-01T14:20:00Z",
            caption = "Golden sunbeams piercing dense spruce canopy.",
            sizeBytes = 6900000L,
            isFavorite = false,
            albumIds = listOf("alb_travel"),
            location = "Norway",
            tags = listOf("Forest", "Sunbeams")
        ),
        PhotoItem(
            id = "photo_4",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.IMAGE,
            filename = "ZERO_IMG_20260828_CyanLagoon.jpg",
            date = "2026-08-28T17:10:00Z",
            caption = "Pristine tropical shoreline before sundown.",
            sizeBytes = 5400000L,
            isFavorite = false,
            albumIds = listOf("alb_travel"),
            location = "Maldives",
            tags = listOf("Beach", "Ocean")
        ),
        PhotoItem(
            id = "photo_5",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.IMAGE,
            filename = "ZERO_VAULT_Confidential_Hardware_Blueprints.png",
            date = "2026-08-15T09:30:00Z",
            caption = "Hardware schematic layout (AES-256 GCM encrypted in private vault)",
            sizeBytes = 9200000L,
            isFavorite = true,
            albumIds = listOf("alb_vault_secure"),
            location = "Secure Lab",
            tags = listOf("Vault", "Encrypted", "Confidential")
        ),
        PhotoItem(
            id = "photo_6",
            userId = "u_zero_me",
            url = "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?auto=format&fit=crop&w=1200&q=80",
            type = MediaType.VIDEO,
            filename = "ZERO_VID_20260820_Highlands4K.mp4",
            date = "2026-08-20T11:00:00Z",
            caption = "Cinematic 60fps drone pass through Scottish glens.",
            sizeBytes = 184000000L,
            isFavorite = true,
            albumIds = listOf("alb_travel", "alb_videos"),
            duration = "0:45",
            location = "Isle of Skye",
            tags = listOf("Drone", "Video", "4K")
        )
    )

    val INITIAL_ALBUMS = listOf(
        Album(
            id = "alb_travel",
            userId = "u_zero_me",
            name = "Wanderlust & Glens",
            coverUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80",
            photoCount = 4,
            description = "Travel expeditions across high altitudes."
        ),
        Album(
            id = "alb_favorites",
            userId = "u_zero_me",
            name = "Starred Favorites",
            coverUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=600&q=80",
            photoCount = 3,
            description = "All bookmarked photos across vaults."
        ),
        Album(
            id = "alb_vault_secure",
            userId = "u_zero_me",
            name = "Secret Private Vault",
            coverUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=600&q=80",
            isPrivate = true,
            isVaultLocked = true,
            pinCode = "1234",
            photoCount = 1,
            description = "Protected behind 4-digit PIN security shield."
        ),
        Album(
            id = "alb_videos",
            userId = "u_zero_me",
            name = "4K Cinematic Reels",
            coverUrl = "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?auto=format&fit=crop&w=600&q=80",
            photoCount = 1,
            description = "Ultra high-definition video captures."
        )
    )

    val INITIAL_POSTS = listOf(
        Post(
            id = "post_1",
            userId = "u_maya",
            user = OTHER_USERS[0],
            content = "Golden hour reflections in Kyoto. Stored directly to my ZERO cloud vault in full uncompressed RAW.",
            mediaUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=1200&q=80",
            createdAt = "2h ago",
            likes = listOf("u_zero_me", "u_elena"),
            comments = listOf(
                Comment(
                    id = "c_1",
                    userId = "u_zero_me",
                    user = CURRENT_USER.toPreview(),
                    text = "The dynamic range in this shot is staggering!",
                    createdAt = "1h ago"
                )
            ),
            sharesCount = 3
        ),
        Post(
            id = "post_2",
            userId = "u_zero_me",
            user = CURRENT_USER.toPreview(),
            content = "Alpine twilight session. Backing up over Wi-Fi with zero compression enabled on the Pro tier.",
            mediaUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=1200&q=80",
            createdAt = "5h ago",
            likes = listOf("u_maya", "u_elena", "u_marcus"),
            comments = listOf(
                Comment(
                    id = "c_2",
                    userId = "u_elena",
                    user = OTHER_USERS[1],
                    text = "Look at those stars! What was your exposure time?",
                    createdAt = "3h ago"
                )
            ),
            sharesCount = 5
        ),
        Post(
            id = "post_3",
            userId = "u_elena",
            user = OTHER_USERS[1],
            content = "Morning hike through misty fjords. End-to-end cloud sync handled all 240 shots before lunch.",
            mediaUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1200&q=80",
            createdAt = "1d ago",
            likes = listOf("u_zero_me"),
            comments = emptyList(),
            sharesCount = 1
        )
    )

    val INITIAL_STORIES = listOf(
        Story(
            id = "story_1",
            userId = "u_maya",
            user = OTHER_USERS[0],
            mediaUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80",
            caption = "Tokyo neon rain streets 🌧️✨"
        ),
        Story(
            id = "story_2",
            userId = "u_elena",
            user = OTHER_USERS[1],
            mediaUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80",
            caption = "Peak summit reached! 3,200m altitude 🏔️"
        ),
        Story(
            id = "story_3",
            userId = "u_marcus",
            user = OTHER_USERS[2],
            mediaUrl = "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?auto=format&fit=crop&w=800&q=80",
            caption = "Sound design setup for the upcoming documentary 🎧"
        )
    )

    val INITIAL_CONVERSATIONS = listOf(
        Conversation(
            otherUser = OTHER_USERS[0],
            lastMessage = Message(
                id = "m_1",
                senderId = "u_maya",
                receiverId = "u_zero_me",
                text = "Hey Kai! Did you check out the new ZERO encryption update?",
                createdAt = "10:30 AM",
                isRead = false
            ),
            unreadCount = 1
        ),
        Conversation(
            otherUser = OTHER_USERS[1],
            lastMessage = Message(
                id = "m_2",
                senderId = "u_zero_me",
                receiverId = "u_elena",
                text = "Yes, shared the alpine folder with you directly.",
                createdAt = "Yesterday",
                isRead = true
            ),
            unreadCount = 0
        )
    )

    val INITIAL_NOTIFICATIONS = listOf(
        NotificationItem(
            id = "notif_1",
            userId = "u_zero_me",
            type = NotificationType.LIKE,
            actor = OTHER_USERS[0],
            message = "liked your post 'Alpine twilight session'",
            createdAt = "25m ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            userId = "u_zero_me",
            type = NotificationType.BACKUP_COMPLETED,
            actor = CURRENT_USER.toPreview(),
            message = "Vault backup verified: 6 items encrypted with AES-GCM",
            createdAt = "2h ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            userId = "u_zero_me",
            type = NotificationType.COMMENT,
            actor = OTHER_USERS[1],
            message = "commented: 'Look at those stars!'",
            createdAt = "3h ago",
            isRead = true
        ),
        NotificationItem(
            id = "notif_4",
            userId = "u_zero_me",
            type = NotificationType.FOLLOW,
            actor = OTHER_USERS[2],
            message = "started following your vault updates",
            createdAt = "1d ago",
            isRead = true
        )
    )
}
