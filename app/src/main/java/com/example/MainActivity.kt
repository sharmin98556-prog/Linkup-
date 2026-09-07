package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.FrendoTab
import com.example.ui.FrendoViewModel
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.CreatePostDialog
import com.example.ui.components.CreateStoryDialog
import com.example.ui.components.DeleteAccountDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.FrendoTopBar
import com.example.ui.components.StoryViewerDialog
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.MessengerScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import android.util.Log
import com.example.ui.theme.FrendoBlue
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Firestore for real-time data syncing
        initFirestore()

        setContent {
            val viewModel: FrendoViewModel = viewModel()
            val appSettings by viewModel.appSettings.collectAsState()

            MyApplicationTheme(darkTheme = appSettings.isDarkMode) {
                FrendoApp(viewModel = viewModel)
            }
        }
    }

    private fun initFirestore() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            val firestore = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                .build()
            firestore.firestoreSettings = settings
            Log.d("FrendoApp", "Firebase Firestore initialized successfully with persistent cache for real-time data syncing.")
        } catch (e: Exception) {
            Log.w("FrendoApp", "Firebase Firestore initialization note: ${e.message}")
        }
    }
}

@Composable
fun FrendoApp(viewModel: FrendoViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val appSettings by viewModel.appSettings.collectAsState()

    val posts by viewModel.allPosts.collectAsState()
    val stories by viewModel.allStories.collectAsState()
    val friends by viewModel.allFriends.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val groups by viewModel.allGroups.collectAsState()

    val activeStory by viewModel.activeStory.collectAsState()
    val activeCommentPostId by viewModel.activeCommentPostId.collectAsState()
    val commentsForPost by viewModel.commentsForActivePost.collectAsState()
    val activeChatFriend by viewModel.activeChatFriend.collectAsState()
    val messagesForFriend by viewModel.messagesForActiveFriend.collectAsState()

    val showCreatePostDialog by viewModel.showCreatePostDialog.collectAsState()
    val showCreateStoryDialog by viewModel.showCreateStoryDialog.collectAsState()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsState()
    val showPasswordDialog by viewModel.showPasswordDialog.collectAsState()
    val showDeleteAccountDialog by viewModel.showDeleteAccountDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (activeChatFriend == null) {
                FrendoTopBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    unreadNotifications = unreadNotifications,
                    onNotificationsClick = { viewModel.selectTab(FrendoTab.NOTIFICATIONS) },
                    onMessengerClick = { viewModel.selectTab(FrendoTab.MESSENGER) },
                    onProfileClick = { viewModel.selectTab(FrendoTab.PROFILE) },
                    userProfile = userProfile
                )
            }
        },
        bottomBar = {
            if (activeChatFriend == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == FrendoTab.FEED,
                        onClick = { viewModel.selectTab(FrendoTab.FEED) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
                        label = { Text("Feed", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_feed")
                    )

                    NavigationBarItem(
                        selected = currentTab == FrendoTab.FRIENDS,
                        onClick = { viewModel.selectTab(FrendoTab.FRIENDS) },
                        icon = { Icon(Icons.Default.People, contentDescription = "Friends") },
                        label = { Text("Friends", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_friends")
                    )

                    NavigationBarItem(
                        selected = currentTab == FrendoTab.STORIES,
                        onClick = { viewModel.selectTab(FrendoTab.STORIES) },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Stories") },
                        label = { Text("Stories", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_stories")
                    )

                    NavigationBarItem(
                        selected = currentTab == FrendoTab.GROUPS,
                        onClick = { viewModel.selectTab(FrendoTab.GROUPS) },
                        icon = { Icon(Icons.Default.Groups, contentDescription = "Groups") },
                        label = { Text("Groups", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_groups")
                    )

                    NavigationBarItem(
                        selected = currentTab == FrendoTab.PROFILE,
                        onClick = { viewModel.selectTab(FrendoTab.PROFILE) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_profile")
                    )

                    NavigationBarItem(
                        selected = currentTab == FrendoTab.SETTINGS,
                        onClick = { viewModel.selectTab(FrendoTab.SETTINGS) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrendoBlue,
                            selectedTextColor = FrendoBlue,
                            indicatorColor = FrendoBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_settings")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                FrendoTab.FEED -> FeedScreen(
                    posts = posts,
                    stories = stories,
                    userProfile = userProfile,
                    searchQuery = searchQuery,
                    onCreateStoryClick = { viewModel.setCreateStoryDialog(true) },
                    onStoryClick = { viewModel.openStory(it) },
                    onComposerClick = { viewModel.setCreatePostDialog(true) },
                    onReactionSelect = { postId, reaction -> viewModel.toggleReaction(postId, reaction) },
                    onCommentsClick = { postId -> viewModel.openComments(postId) },
                    onShareClick = { postId ->
                        scope.launch { snackbarHostState.showSnackbar("Post shared to your timeline! 🚀") }
                    },
                    onSaveClick = { postId ->
                        viewModel.toggleSavePost(postId)
                        scope.launch { snackbarHostState.showSnackbar("Post saved to bookmarks.") }
                    },
                    onDeleteClick = { postId ->
                        viewModel.deletePost(postId)
                        scope.launch { snackbarHostState.showSnackbar("Post deleted.") }
                    }
                )

                FrendoTab.FRIENDS -> FriendsScreen(
                    friends = friends,
                    onAcceptRequest = { viewModel.acceptFriend(it) },
                    onDeclineRequest = { viewModel.declineFriend(it) },
                    onSendRequest = { viewModel.sendFriendRequest(it) },
                    onUnfriend = { viewModel.unfriend(it) },
                    onOpenChat = {
                        viewModel.selectTab(FrendoTab.MESSENGER)
                        viewModel.openChat(it)
                    }
                )

                FrendoTab.STORIES -> FeedScreen(
                    posts = posts.filter { it.imageResName != null },
                    stories = stories,
                    userProfile = userProfile,
                    searchQuery = "",
                    onCreateStoryClick = { viewModel.setCreateStoryDialog(true) },
                    onStoryClick = { viewModel.openStory(it) },
                    onComposerClick = { viewModel.setCreateStoryDialog(true) },
                    onReactionSelect = { postId, reaction -> viewModel.toggleReaction(postId, reaction) },
                    onCommentsClick = { postId -> viewModel.openComments(postId) },
                    onShareClick = { postId ->
                        scope.launch { snackbarHostState.showSnackbar("Story shared!") }
                    },
                    onSaveClick = { postId -> viewModel.toggleSavePost(postId) },
                    onDeleteClick = { postId -> viewModel.deletePost(postId) }
                )

                FrendoTab.GROUPS -> GroupsScreen(
                    groups = groups,
                    onToggleJoin = { viewModel.toggleJoinGroup(it) }
                )

                FrendoTab.MESSENGER -> MessengerScreen(
                    friends = friends,
                    activeChatFriend = activeChatFriend,
                    messages = messagesForFriend,
                    onOpenChat = { viewModel.openChat(it) },
                    onCloseChat = { viewModel.closeChat() },
                    onSendMessage = { friend, text -> viewModel.sendMessage(friend, text) }
                )

                FrendoTab.NOTIFICATIONS -> NotificationsScreen(
                    notifications = notifications,
                    onMarkAllRead = { viewModel.selectTab(FrendoTab.NOTIFICATIONS) }
                )

                FrendoTab.PROFILE -> ProfileScreen(
                    userProfile = userProfile,
                    userPosts = posts.filter { it.authorName == userProfile.name },
                    onAddStoryClick = { viewModel.setCreateStoryDialog(true) },
                    onEditProfileClick = { viewModel.setEditProfileDialog(true) },
                    onReactionSelect = { postId, reaction -> viewModel.toggleReaction(postId, reaction) },
                    onCommentsClick = { postId -> viewModel.openComments(postId) },
                    onShareClick = { postId ->
                        scope.launch { snackbarHostState.showSnackbar("Post shared to timeline!") }
                    },
                    onSaveClick = { postId -> viewModel.toggleSavePost(postId) },
                    onDeleteClick = { postId -> viewModel.deletePost(postId) }
                )

                FrendoTab.SETTINGS -> SettingsScreen(
                    appSettings = appSettings,
                    onGeneralSave = { name, email -> viewModel.updateGeneralSettings(name, email) },
                    onChangePasswordClick = { viewModel.setPasswordDialog(true) },
                    onPrivacySave = { postPrivacy, friendReq, emailLookup ->
                        viewModel.updatePrivacySettings(postPrivacy, friendReq, emailLookup)
                    },
                    onNotificationToggle = { viewModel.toggleNotificationSetting(it) },
                    onBlockUser = { viewModel.blockUser(it) },
                    onUnblockUser = { viewModel.unblockUser(it) },
                    onLogoutAllSessions = { viewModel.logoutAllOtherSessions() },
                    onThemeToggle = { viewModel.toggleDarkMode(it) },
                    onLanguageChange = { viewModel.setLanguage(it) },
                    onDeleteAccountClick = { viewModel.setDeleteAccountDialog(true) }
                )
            }
        }
    }

    // Active Story Viewer Modal
    activeStory?.let { story ->
        StoryViewerDialog(
            story = story,
            onDismiss = { viewModel.closeStory() }
        )
    }

    // Comments Bottom Sheet
    if (activeCommentPostId != null) {
        CommentsBottomSheet(
            postId = activeCommentPostId!!,
            comments = commentsForPost,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(activeCommentPostId!!, text) },
            onToggleLike = { commentId -> viewModel.toggleCommentLike(commentId) }
        )
    }

    // Create Post Dialog
    if (showCreatePostDialog) {
        CreatePostDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.setCreatePostDialog(false) },
            onPostCreated = { content, imageRes, privacy, feeling ->
                viewModel.createPost(content, imageRes, privacy, feeling)
                scope.launch { snackbarHostState.showSnackbar("Post published successfully!") }
            }
        )
    }

    // Create Story Dialog
    if (showCreateStoryDialog) {
        CreateStoryDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.setCreateStoryDialog(false) },
            onStoryCreated = { caption, imageRes, startColor, endColor ->
                viewModel.createStory(caption, imageRes, startColor, endColor)
                scope.launch { snackbarHostState.showSnackbar("Added to your story!") }
            }
        )
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.setEditProfileDialog(false) },
            onSave = { name, bio, location, work ->
                viewModel.saveProfile(name, bio, location, work)
                scope.launch { snackbarHostState.showSnackbar("Profile updated!") }
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteAccountDialog) {
        DeleteAccountDialog(
            onDismiss = { viewModel.setDeleteAccountDialog(false) },
            onConfirm = {
                viewModel.setDeleteAccountDialog(false)
                scope.launch { snackbarHostState.showSnackbar("Account deactivated.") }
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
