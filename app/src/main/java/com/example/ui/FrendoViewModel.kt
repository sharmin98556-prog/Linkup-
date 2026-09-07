package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FrendoDatabase
import com.example.data.model.AppSettingsEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FriendEntity
import com.example.data.model.GroupEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import com.example.data.repository.FrendoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FrendoTab {
    FEED,
    FRIENDS,
    STORIES,
    GROUPS,
    MESSENGER,
    NOTIFICATIONS,
    PROFILE,
    SETTINGS
}

data class UiReaction(
    val type: String,
    val emoji: String,
    val label: String
)

val REACTION_OPTIONS = listOf(
    UiReaction("LIKE", "👍", "Like"),
    UiReaction("LOVE", "❤️", "Love"),
    UiReaction("HAHA", "😂", "Haha"),
    UiReaction("WOW", "😮", "Wow"),
    UiReaction("SAD", "😢", "Sad"),
    UiReaction("ANGRY", "😡", "Angry")
)

class FrendoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FrendoRepository

    init {
        val database = FrendoDatabase.getDatabase(application)
        repository = FrendoRepository(database.frendoDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation State
    private val _currentTab = MutableStateFlow(FrendoTab.FEED)
    val currentTab: StateFlow<FrendoTab> = _currentTab.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Chat Friend
    private val _activeChatFriend = MutableStateFlow<FriendEntity?>(null)
    val activeChatFriend: StateFlow<FriendEntity?> = _activeChatFriend.asStateFlow()

    // Active Comments Post
    private val _activeCommentPostId = MutableStateFlow<Long?>(null)
    val activeCommentPostId: StateFlow<Long?> = _activeCommentPostId.asStateFlow()

    // Active Story Viewer
    private val _activeStory = MutableStateFlow<StoryEntity?>(null)
    val activeStory: StateFlow<StoryEntity?> = _activeStory.asStateFlow()

    // Dialog States
    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    private val _showCreateStoryDialog = MutableStateFlow(false)
    val showCreateStoryDialog: StateFlow<Boolean> = _showCreateStoryDialog.asStateFlow()

    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _showPasswordDialog = MutableStateFlow(false)
    val showPasswordDialog: StateFlow<Boolean> = _showPasswordDialog.asStateFlow()

    private val _showDeleteAccountDialog = MutableStateFlow(false)
    val showDeleteAccountDialog: StateFlow<Boolean> = _showDeleteAccountDialog.asStateFlow()

    // Data Streams from Repository
    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val appSettings: StateFlow<AppSettingsEntity> = repository.appSettings
        .map { it ?: AppSettingsEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettingsEntity())

    val allPosts: StateFlow<List<PostEntity>> = combine(
        repository.allPosts,
        _searchQuery
    ) { posts, query ->
        if (query.isBlank()) posts
        else posts.filter {
            it.content.contains(query, ignoreCase = true) ||
            it.authorName.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFriends: StateFlow<List<FriendEntity>> = combine(
        repository.allFriends,
        _searchQuery
    ) { friends, query ->
        if (query.isBlank()) friends
        else friends.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.handle.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.allNotifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allGroups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Comments Stream
    private val _commentsForActivePost = MutableStateFlow<List<CommentEntity>>(emptyList())
    val commentsForActivePost: StateFlow<List<CommentEntity>> = _commentsForActivePost.asStateFlow()

    // Active Messages Stream
    private val _messagesForActiveFriend = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messagesForActiveFriend: StateFlow<List<MessageEntity>> = _messagesForActiveFriend.asStateFlow()

    // Navigation and Action handlers
    fun selectTab(tab: FrendoTab) {
        _currentTab.value = tab
        if (tab == FrendoTab.NOTIFICATIONS) {
            viewModelScope.launch {
                repository.markAllNotificationsAsRead()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openChat(friend: FriendEntity) {
        _activeChatFriend.value = friend
        viewModelScope.launch {
            repository.getMessagesForFriend(friend.id).collect { messages ->
                _messagesForActiveFriend.value = messages
            }
        }
    }

    fun closeChat() {
        _activeChatFriend.value = null
        _messagesForActiveFriend.value = emptyList()
    }

    fun sendMessage(friend: FriendEntity, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(friend.id, friend.name, text.trim())
        }
    }

    fun openComments(postId: Long) {
        _activeCommentPostId.value = postId
        viewModelScope.launch {
            repository.getCommentsForPost(postId).collect { comments ->
                _commentsForActivePost.value = comments
            }
        }
    }

    fun closeComments() {
        _activeCommentPostId.value = null
        _commentsForActivePost.value = emptyList()
    }

    fun addComment(postId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, text.trim())
        }
    }

    fun toggleCommentLike(commentId: Long) {
        viewModelScope.launch {
            repository.toggleCommentLike(commentId)
        }
    }

    fun toggleReaction(postId: Long, reactionType: String) {
        viewModelScope.launch {
            val post = allPosts.value.find { it.id == postId }
            val newReaction = if (post?.userReaction == reactionType) null else reactionType
            repository.updateReaction(postId, newReaction)
        }
    }

    fun toggleSavePost(postId: Long) {
        viewModelScope.launch {
            repository.toggleSavePost(postId)
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            repository.deletePost(postId)
        }
    }

    fun createPost(content: String, imageResName: String?, privacy: String, feeling: String?, groupName: String? = null) {
        if (content.isBlank() && imageResName == null) return
        viewModelScope.launch {
            repository.createPost(
                content = content.trim(),
                imageResName = imageResName,
                privacy = privacy,
                feeling = feeling,
                groupName = groupName
            )
            _showCreatePostDialog.value = false
        }
    }

    fun openStory(story: StoryEntity) {
        _activeStory.value = story
        viewModelScope.launch {
            repository.markStoryViewed(story.id)
        }
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun createStory(caption: String?, imageResName: String?, startColor: Long, endColor: Long) {
        viewModelScope.launch {
            repository.createStory(caption, imageResName, startColor, endColor)
            _showCreateStoryDialog.value = false
        }
    }

    // Friend Actions
    fun acceptFriend(friendId: Long) {
        viewModelScope.launch {
            repository.acceptFriendRequest(friendId)
        }
    }

    fun declineFriend(friendId: Long) {
        viewModelScope.launch {
            repository.declineFriendRequest(friendId)
        }
    }

    fun sendFriendRequest(friendId: Long) {
        viewModelScope.launch {
            repository.sendFriendRequest(friendId)
        }
    }

    fun unfriend(friendId: Long) {
        viewModelScope.launch {
            repository.removeFriend(friendId)
        }
    }

    // Group Actions
    fun toggleJoinGroup(groupId: Long) {
        viewModelScope.launch {
            repository.toggleGroupMembership(groupId)
        }
    }

    // Profile & Dialogs
    fun setCreatePostDialog(open: Boolean) {
        _showCreatePostDialog.value = open
    }

    fun setCreateStoryDialog(open: Boolean) {
        _showCreateStoryDialog.value = open
    }

    fun setEditProfileDialog(open: Boolean) {
        _showEditProfileDialog.value = open
    }

    fun setPasswordDialog(open: Boolean) {
        _showPasswordDialog.value = open
    }

    fun setDeleteAccountDialog(open: Boolean) {
        _showDeleteAccountDialog.value = open
    }

    fun saveProfile(name: String, bio: String, location: String, work: String) {
        viewModelScope.launch {
            repository.updateUserProfile(name.trim(), bio.trim(), location.trim(), work.trim())
            _showEditProfileDialog.value = false
        }
    }

    // Settings Updates
    fun updateGeneralSettings(name: String, email: String) {
        viewModelScope.launch {
            val current = appSettings.value
            repository.updateSettings(current.copy(userName = name, userEmail = email))
        }
    }

    fun updatePrivacySettings(postPrivacy: String, friendRequestPerm: String, emailLookupPerm: String) {
        viewModelScope.launch {
            val current = appSettings.value
            repository.updateSettings(
                current.copy(
                    postPrivacy = postPrivacy,
                    friendRequestPermission = friendRequestPerm,
                    emailLookupPermission = emailLookupPerm
                )
            )
        }
    }

    fun toggleNotificationSetting(type: String) {
        viewModelScope.launch {
            val current = appSettings.value
            val updated = when (type) {
                "email" -> current.copy(emailNotifications = !current.emailNotifications)
                "push" -> current.copy(pushNotifications = !current.pushNotifications)
                "sound" -> current.copy(inAppSounds = !current.inAppSounds)
                else -> current
            }
            repository.updateSettings(updated)
        }
    }

    fun unblockUser(username: String) {
        viewModelScope.launch {
            val current = appSettings.value
            val list = current.blockedUsers.split(",").filter { it.isNotBlank() && it != username }
            repository.updateSettings(current.copy(blockedUsers = list.joinToString(",")))
        }
    }

    fun blockUser(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            val current = appSettings.value
            val list = current.blockedUsers.split(",").filter { it.isNotBlank() }.toMutableList()
            if (!list.contains(username.trim())) {
                list.add(username.trim())
            }
            repository.updateSettings(current.copy(blockedUsers = list.joinToString(",")))
        }
    }

    fun logoutAllOtherSessions() {
        viewModelScope.launch {
            val current = appSettings.value
            repository.updateSettings(current.copy(activeSessions = "Pixel 9 Pro (This device - Active now)"))
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            val current = appSettings.value
            repository.updateSettings(current.copy(isDarkMode = isDark))
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            val current = appSettings.value
            repository.updateSettings(current.copy(language = lang))
        }
    }
}
