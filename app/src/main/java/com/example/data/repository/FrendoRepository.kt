package com.example.data.repository

import com.example.data.local.FrendoDao
import com.example.data.model.AppSettingsEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FriendEntity
import com.example.data.model.GroupEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FrendoRepository(private val dao: FrendoDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val allStories: Flow<List<StoryEntity>> = dao.getAllStories()
    val allFriends: Flow<List<FriendEntity>> = dao.getAllFriends()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val allGroups: Flow<List<GroupEntity>> = dao.getAllGroups()
    val appSettings: Flow<AppSettingsEntity?> = dao.getSettings()

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)

    fun getMessagesForFriend(friendId: Long): Flow<List<MessageEntity>> = dao.getMessagesForFriend(friendId)

    suspend fun checkAndSeedInitialData() {
        val existingProfile = dao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            // Seed User Profile
            val defaultProfile = UserProfile(
                id = 1L,
                name = "Alex Morgan",
                handle = "@alexm",
                email = "alex.morgan@frendo.app",
                bio = "Digital creator & mobile architect 📱 Building friendly communities & crafting code. Coffee enthusiast ☕",
                location = "San Francisco, CA",
                work = "Lead Product Engineer at Frendo",
                joinedDate = "Joined September 2024",
                friendsCount = 348,
                followersCount = 1420,
                avatarRes = "img_avatar_alex",
                coverRes = "img_cover_nature"
            )
            dao.insertUserProfile(defaultProfile)

            // Seed Settings
            dao.insertSettings(AppSettingsEntity(id = 1L))

            val now = System.currentTimeMillis()

            // Seed Stories
            val stories = listOf(
                StoryEntity(
                    authorName = "Your Story",
                    authorAvatarRes = "img_avatar_alex",
                    textCaption = "Morning coffee vibes in the new studio! ☕☀️",
                    imageResName = "img_post_desk",
                    gradientStartHex = 0xFF2563EB,
                    gradientEndHex = 0xFF3B82F6,
                    timestamp = now - 3600000,
                    isUserStory = true
                ),
                StoryEntity(
                    authorName = "Sarah Chen",
                    authorAvatarRes = "sarah",
                    textCaption = "Hiking through redwood trails today 🌲✨",
                    imageResName = "img_cover_nature",
                    gradientStartHex = 0xFF059669,
                    gradientEndHex = 0xFF10B981,
                    timestamp = now - 7200000
                ),
                StoryEntity(
                    authorName = "David Kim",
                    authorAvatarRes = "david",
                    textCaption = "Just launched our open-source project! 🚀 Check it out",
                    gradientStartHex = 0xFF7C3AED,
                    gradientEndHex = 0xFF8B5CF6,
                    timestamp = now - 14400000
                ),
                StoryEntity(
                    authorName = "Elena Rostova",
                    authorAvatarRes = "elena",
                    textCaption = "Weekend baking experiment: matcha croissants 🥐",
                    gradientStartHex = 0xFFDB2777,
                    gradientEndHex = 0xFFF43F5E,
                    timestamp = now - 21600000
                ),
                StoryEntity(
                    authorName = "Marcus Brody",
                    authorAvatarRes = "marcus",
                    textCaption = "Sunsets over the golden gate bridge never get old 🌅",
                    gradientStartHex = 0xFFD97706,
                    gradientEndHex = 0xFFF59E0B,
                    timestamp = now - 28800000
                )
            )
            stories.forEach { dao.insertStory(it) }

            // Seed Friends
            val friends = listOf(
                FriendEntity(
                    name = "Sarah Chen",
                    handle = "@sarahc",
                    avatarRes = "sarah",
                    mutualFriends = 24,
                    status = "ACCEPTED",
                    isOnline = true,
                    lastActiveText = "Active now",
                    bio = "Photographer & UI Explorer"
                ),
                FriendEntity(
                    name = "David Kim",
                    handle = "@davidk",
                    avatarRes = "david",
                    mutualFriends = 18,
                    status = "ACCEPTED",
                    isOnline = true,
                    lastActiveText = "Active now",
                    bio = "Open source contributor & runner"
                ),
                FriendEntity(
                    name = "Elena Rostova",
                    handle = "@elenar",
                    avatarRes = "elena",
                    mutualFriends = 12,
                    status = "ACCEPTED",
                    isOnline = false,
                    lastActiveText = "Active 25m ago",
                    bio = "Pastry chef & food blogger"
                ),
                FriendEntity(
                    name = "Marcus Brody",
                    handle = "@marcusb",
                    avatarRes = "marcus",
                    mutualFriends = 31,
                    status = "ACCEPTED",
                    isOnline = true,
                    lastActiveText = "Active now",
                    bio = "Sound designer & music enthusiast"
                ),
                FriendEntity(
                    name = "Jessica Taylor",
                    handle = "@jtaylor",
                    avatarRes = "jessica",
                    mutualFriends = 9,
                    status = "REQUEST_RECEIVED",
                    isOnline = false,
                    lastActiveText = "Active 2h ago",
                    bio = "Software architect & cyclist"
                ),
                FriendEntity(
                    name = "Liam O'Connor",
                    handle = "@liamo",
                    avatarRes = "liam",
                    mutualFriends = 5,
                    status = "REQUEST_RECEIVED",
                    isOnline = true,
                    lastActiveText = "Active now",
                    bio = "Writer & indie game creator"
                ),
                FriendEntity(
                    name = "Chloe Dupont",
                    handle = "@chloed",
                    avatarRes = "chloe",
                    mutualFriends = 14,
                    status = "SUGGESTION",
                    isOnline = true,
                    lastActiveText = "Active now",
                    bio = "Graphic designer based in Paris"
                ),
                FriendEntity(
                    name = "Arjun Patel",
                    handle = "@arjunp",
                    avatarRes = "arjun",
                    mutualFriends = 8,
                    status = "SUGGESTION",
                    isOnline = false,
                    lastActiveText = "Active 1d ago",
                    bio = "Robotics engineer & tech enthusiast"
                )
            )
            dao.insertFriends(friends)

            // Seed Groups
            val groups = listOf(
                GroupEntity(
                    name = "Mobile App Creators & Innovators",
                    category = "Technology",
                    membersCount = 14280,
                    description = "A community for mobile engineers, designers, and founders to share progress, feedback, and architecture tips.",
                    isJoined = true,
                    bannerColorHex = 0xFF2563EB
                ),
                GroupEntity(
                    name = "Urban & Landscape Photography",
                    category = "Art & Photography",
                    membersCount = 8920,
                    description = "Share your best shots, camera gear discussions, composition advice, and weekly photography challenges.",
                    isJoined = true,
                    bannerColorHex = 0xFF059669
                ),
                GroupEntity(
                    name = "Specialty Coffee Roasters & Baristas",
                    category = "Food & Drink",
                    membersCount = 5340,
                    description = "For everyone who loves pour-over methods, aeropress recipes, latte art, and single origin beans.",
                    isJoined = false,
                    bannerColorHex = 0xFFB45309
                ),
                GroupEntity(
                    name = "San Francisco Tech & Design Meetups",
                    category = "Local Community",
                    membersCount = 6120,
                    description = "Local meetups, hackathons, and networking evenings across the Bay Area.",
                    isJoined = true,
                    bannerColorHex = 0xFF7C3AED
                )
            )
            dao.insertGroups(groups)

            // Seed Posts
            val post1Id = dao.insertPost(
                PostEntity(
                    authorName = "Alex Morgan",
                    authorHandle = "@alexm",
                    authorAvatarRes = "img_avatar_alex",
                    timestamp = now - 1800000,
                    content = "Excited to share the updated workspace setup! Natural light, warm latte, and quiet morning hours are truly the best recipe for creative flow. What does your productive sanctuary look like?",
                    imageResName = "img_post_desk",
                    privacy = "Public",
                    feeling = "☕ feeling productive",
                    likeCount = 42,
                    loveCount = 18,
                    hahaCount = 2,
                    userReaction = "LIKE",
                    commentsCount = 2,
                    sharesCount = 5
                )
            )

            // Seed Comments for post1
            dao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorName = "Sarah Chen",
                    authorAvatarRes = "sarah",
                    text = "That warm lighting is absolute perfection! Where did you get that wooden monitor stand?",
                    timestamp = now - 1200000,
                    likeCount = 4,
                    isLiked = true
                )
            )
            dao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorName = "David Kim",
                    authorAvatarRes = "david",
                    text = "Morning coffee routines make all the difference. Clean setup mate!",
                    timestamp = now - 600000,
                    likeCount = 2,
                    isLiked = false
                )
            )

            val post2Id = dao.insertPost(
                PostEntity(
                    authorName = "Sarah Chen",
                    authorAvatarRes = "sarah",
                    authorHandle = "@sarahc",
                    timestamp = now - 10800000,
                    content = "Breathtaking morning hike above the fog line in Marin County. The blue ridges and early golden glow reminded me why I fell in love with outdoor photography. Nature is the ultimate artist! 🌲⛰️",
                    imageResName = "img_cover_nature",
                    privacy = "Public",
                    feeling = "✨ feeling inspired",
                    likeCount = 89,
                    loveCount = 45,
                    wowCount = 12,
                    userReaction = "LOVE",
                    commentsCount = 1,
                    sharesCount = 11
                )
            )

            dao.insertComment(
                CommentEntity(
                    postId = post2Id,
                    authorName = "Marcus Brody",
                    authorAvatarRes = "marcus",
                    text = "The contrast between the peaks and the clouds is stunning Sarah! Great capture.",
                    timestamp = now - 9000000,
                    likeCount = 3,
                    isLiked = false
                )
            )

            dao.insertPost(
                PostEntity(
                    authorName = "David Kim",
                    authorAvatarRes = "david",
                    authorHandle = "@davidk",
                    timestamp = now - 28800000,
                    content = "Just shipped version 2.0 of our community open-source toolkit. Complete zero-dependency design, full reactive streams, and comprehensive offline first sync! Huge thanks to everyone who tested and submitted feedback! 🚀🎉",
                    privacy = "Public",
                    feeling = "🎉 celebrating an achievement",
                    likeCount = 63,
                    loveCount = 29,
                    wowCount = 7,
                    userReaction = null,
                    commentsCount = 0,
                    sharesCount = 8,
                    groupName = "Mobile App Creators & Innovators"
                )
            )

            // Seed Messages
            dao.insertMessage(
                MessageEntity(
                    friendId = 1L,
                    friendName = "Sarah Chen",
                    senderName = "Sarah Chen",
                    text = "Hey Alex! Loved your workspace post earlier.",
                    timestamp = now - 3600000,
                    isFromMe = false
                )
            )
            dao.insertMessage(
                MessageEntity(
                    friendId = 1L,
                    friendName = "Sarah Chen",
                    senderName = "Alex Morgan",
                    text = "Thanks Sarah! Taking advantage of the morning sun.",
                    timestamp = now - 3000000,
                    isFromMe = true
                )
            )
            dao.insertMessage(
                MessageEntity(
                    friendId = 1L,
                    friendName = "Sarah Chen",
                    senderName = "Sarah Chen",
                    text = "Are we still meeting for coffee on Thursday to review the designs?",
                    timestamp = now - 1800000,
                    isFromMe = false
                )
            )

            dao.insertMessage(
                MessageEntity(
                    friendId = 2L,
                    friendName = "David Kim",
                    senderName = "David Kim",
                    text = "Yo Alex, did you check out the new repository update?",
                    timestamp = now - 7200000,
                    isFromMe = false
                )
            )

            // Seed Notifications
            val notifications = listOf(
                NotificationEntity(
                    type = "REACTION",
                    actorName = "Sarah Chen",
                    actorAvatarRes = "sarah",
                    contentText = "loved your post: \"Excited to share the updated workspace setup...\"",
                    timestamp = now - 1200000,
                    isRead = false,
                    targetPostId = post1Id
                ),
                NotificationEntity(
                    type = "COMMENT",
                    actorName = "David Kim",
                    actorAvatarRes = "david",
                    contentText = "commented: \"Morning coffee routines make all the difference...\"",
                    timestamp = now - 600000,
                    isRead = false,
                    targetPostId = post1Id
                ),
                NotificationEntity(
                    type = "FRIEND_REQUEST",
                    actorName = "Jessica Taylor",
                    actorAvatarRes = "jessica",
                    contentText = "sent you a friend request. You have 9 mutual friends.",
                    timestamp = now - 14400000,
                    isRead = false
                ),
                NotificationEntity(
                    type = "GROUP",
                    actorName = "Mobile App Creators & Innovators",
                    actorAvatarRes = "group",
                    contentText = "David Kim published a new post in the group.",
                    timestamp = now - 28800000,
                    isRead = true
                )
            )
            dao.insertNotifications(notifications)
        }
    }

    // === Post Actions ===
    suspend fun createPost(
        content: String,
        imageResName: String? = null,
        privacy: String = "Public",
        feeling: String? = null,
        groupName: String? = null
    ): Long {
        val user = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val post = PostEntity(
            authorName = user.name,
            authorHandle = user.handle,
            authorAvatarRes = user.avatarRes,
            timestamp = System.currentTimeMillis(),
            content = content,
            imageResName = imageResName,
            privacy = privacy,
            feeling = feeling,
            groupName = groupName
        )
        return dao.insertPost(post)
    }

    suspend fun updateReaction(postId: Long, newReaction: String?) {
        val post = dao.getPostById(postId).firstOrNull() ?: return
        val currentReaction = post.userReaction

        // Decrement previous reaction count if any
        var like = post.likeCount
        var love = post.loveCount
        var haha = post.hahaCount
        var wow = post.wowCount
        var sad = post.sadCount
        var angry = post.angryCount

        when (currentReaction) {
            "LIKE" -> like = maxOf(0, like - 1)
            "LOVE" -> love = maxOf(0, love - 1)
            "HAHA" -> haha = maxOf(0, haha - 1)
            "WOW" -> wow = maxOf(0, wow - 1)
            "SAD" -> sad = maxOf(0, sad - 1)
            "ANGRY" -> angry = maxOf(0, angry - 1)
        }

        // Increment new reaction count if not un-reacting
        if (newReaction != null) {
            when (newReaction) {
                "LIKE" -> like += 1
                "LOVE" -> love += 1
                "HAHA" -> haha += 1
                "WOW" -> wow += 1
                "SAD" -> sad += 1
                "ANGRY" -> angry += 1
            }
        }

        dao.updatePost(
            post.copy(
                userReaction = newReaction,
                likeCount = like,
                loveCount = love,
                hahaCount = haha,
                wowCount = wow,
                sadCount = sad,
                angryCount = angry
            )
        )
    }

    suspend fun addComment(postId: Long, text: String): Long {
        val user = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val comment = CommentEntity(
            postId = postId,
            authorName = user.name,
            authorAvatarRes = user.avatarRes,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        val commentId = dao.insertComment(comment)
        dao.incrementPostCommentCount(postId)
        return commentId
    }

    suspend fun toggleCommentLike(commentId: Long) {
        dao.toggleCommentLike(commentId)
    }

    suspend fun toggleSavePost(postId: Long) {
        dao.toggleSavePost(postId)
    }

    suspend fun deletePost(postId: Long) {
        dao.deletePost(postId)
    }

    // === Stories ===
    suspend fun createStory(text: String?, imageResName: String?, startColor: Long, endColor: Long) {
        val user = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val story = StoryEntity(
            authorName = "Your Story",
            authorAvatarRes = user.avatarRes,
            textCaption = text,
            imageResName = imageResName,
            gradientStartHex = startColor,
            gradientEndHex = endColor,
            timestamp = System.currentTimeMillis(),
            isUserStory = true
        )
        dao.insertStory(story)
    }

    suspend fun markStoryViewed(storyId: Long) {
        dao.markStoryViewed(storyId)
    }

    // === Friends ===
    suspend fun acceptFriendRequest(friendId: Long) {
        dao.updateFriendStatus(friendId, "ACCEPTED")
    }

    suspend fun declineFriendRequest(friendId: Long) {
        dao.deleteFriend(friendId)
    }

    suspend fun sendFriendRequest(friendId: Long) {
        dao.updateFriendStatus(friendId, "REQUEST_SENT")
    }

    suspend fun removeFriend(friendId: Long) {
        dao.deleteFriend(friendId)
    }

    // === Messaging ===
    suspend fun sendMessage(friendId: Long, friendName: String, text: String) {
        val user = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val message = MessageEntity(
            friendId = friendId,
            friendName = friendName,
            senderName = user.name,
            text = text,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
        dao.insertMessage(message)
    }

    // === Notifications ===
    suspend fun markAllNotificationsAsRead() {
        dao.markAllNotificationsAsRead()
    }

    // === Groups ===
    suspend fun toggleGroupMembership(groupId: Long) {
        dao.toggleGroupMembership(groupId)
    }

    // === User & Settings ===
    suspend fun updateUserProfile(name: String, bio: String, location: String, work: String) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.updateUserProfile(
            current.copy(
                name = name,
                bio = bio,
                location = location,
                work = work
            )
        )
    }

    suspend fun updateSettings(settings: AppSettingsEntity) {
        dao.updateSettings(settings)
    }
}
