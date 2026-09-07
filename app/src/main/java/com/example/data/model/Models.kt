package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey val id: Long = 1L,
    val name: String = "Alex Morgan",
    val handle: String = "@alexm",
    val email: String = "alex.morgan@frendo.app",
    val bio: String = "Digital creator & mobile architect 📱 Building friendly communities & crafting code. Coffee enthusiast ☕",
    val location: String = "San Francisco, CA",
    val work: String = "Lead Product Engineer",
    val joinedDate: String = "Joined September 2024",
    val friendsCount: Int = 348,
    val followersCount: Int = 1420,
    val avatarRes: String = "img_avatar_alex",
    val coverRes: String = "img_cover_nature"
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: String,
    val timestamp: Long,
    val content: String,
    val imageResName: String? = null,
    val privacy: String = "Public", // "Public", "Friends", "Only Me"
    val feeling: String? = null, // e.g., "😊 feeling excited", "☕ having coffee"
    val likeCount: Int = 0,
    val loveCount: Int = 0,
    val hahaCount: Int = 0,
    val wowCount: Int = 0,
    val sadCount: Int = 0,
    val angryCount: Int = 0,
    val userReaction: String? = null, // "LIKE", "LOVE", "HAHA", "WOW", "SAD", "ANGRY", null
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val groupName: String? = null,
    val isSaved: Boolean = false
) {
    val totalReactions: Int
        get() = likeCount + loveCount + hahaCount + wowCount + sadCount + angryCount
}

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val postId: Long,
    val authorName: String,
    val authorAvatarRes: String,
    val text: String,
    val timestamp: Long,
    val likeCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val authorName: String,
    val authorAvatarRes: String,
    val textCaption: String? = null,
    val imageResName: String? = null,
    val gradientStartHex: Long = 0xFF2563EB,
    val gradientEndHex: Long = 0xFF7C3AED,
    val timestamp: Long,
    val isViewed: Boolean = false,
    val isUserStory: Boolean = false
)

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val handle: String,
    val avatarRes: String,
    val mutualFriends: Int = 0,
    val status: String = "ACCEPTED", // "ACCEPTED", "REQUEST_RECEIVED", "REQUEST_SENT", "SUGGESTION"
    val isOnline: Boolean = false,
    val lastActiveText: String = "Active now",
    val bio: String = ""
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val friendId: Long,
    val friendName: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val isFromMe: Boolean,
    val isRead: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val type: String, // "REACTION", "COMMENT", "FRIEND_REQUEST", "GROUP", "STORY"
    val actorName: String,
    val actorAvatarRes: String,
    val contentText: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val targetPostId: Long? = null
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val category: String,
    val membersCount: Int,
    val description: String,
    val isJoined: Boolean = false,
    val bannerColorHex: Long = 0xFF2563EB
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Long = 1L,
    // General
    val userName: String = "Alex Morgan",
    val userEmail: String = "alex.morgan@frendo.app",
    // Privacy
    val postPrivacy: String = "Public", // "Public", "Friends", "Only Me"
    val friendRequestPermission: String = "Everyone", // "Everyone", "Friends of Friends"
    val emailLookupPermission: String = "Everyone", // "Everyone", "Friends", "Only Me"
    // Notifications
    val emailNotifications: Boolean = true,
    val pushNotifications: Boolean = true,
    val inAppSounds: Boolean = true,
    // Blocking
    val blockedUsers: String = "SpamBot99,TrollAccount",
    // Security
    val activeSessions: String = "Pixel 9 Pro (Active now)|Chrome MacOS (2 hours ago)|iPad Pro (Yesterday)",
    // Appearance & Locale
    val isDarkMode: Boolean = false,
    val language: String = "English (US)"
)
