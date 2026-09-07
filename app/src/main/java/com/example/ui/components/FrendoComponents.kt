package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.CommentEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import com.example.ui.REACTION_OPTIONS
import com.example.ui.theme.FrendoAccentGreen
import com.example.ui.theme.FrendoBlue
import com.example.ui.theme.ReactionAngryOrange
import com.example.ui.theme.ReactionHahaYellow
import com.example.ui.theme.ReactionLikeBlue
import com.example.ui.theme.ReactionLoveRed
import com.example.ui.theme.ReactionSadYellow
import com.example.ui.theme.ReactionWowYellow
import kotlinx.coroutines.delay

@Composable
fun FrendoWordmark(
    modifier: Modifier = Modifier,
    fontSize: Int = 26,
    color: Color = FrendoBlue
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "frendo",
            fontSize = fontSize.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            color = color,
            fontFamily = FontFamily.SansSerif
        )
        Box(
            modifier = Modifier
                .padding(start = 2.dp, bottom = 4.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(FrendoAccentGreen)
        )
    }
}

@Composable
fun FrendoAvatar(
    avatarResName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    showOnlineBadge: Boolean = false,
    isOnline: Boolean = false,
    contentDescription: String = "User avatar"
) {
    val context = LocalContext.current
    val imageRes = when (avatarResName) {
        "img_avatar_alex" -> R.drawable.img_avatar_alex
        "img_post_desk" -> R.drawable.img_post_desk
        "img_cover_nature" -> R.drawable.img_cover_nature
        else -> R.drawable.img_avatar_alex
    }

    Box(modifier = modifier.size(size)) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageRes)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
        )

        if (showOnlineBadge && isOnline) {
            Box(
                modifier = Modifier
                    .size(size / 3.4f)
                    .clip(CircleShape)
                    .background(FrendoAccentGreen)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun FrendoTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    unreadNotifications: Int,
    onNotificationsClick: () -> Unit,
    onMessengerClick: () -> Unit,
    onProfileClick: () -> Unit,
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FrendoWordmark(
                    fontSize = 24,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Messenger Icon Button with badge
                    IconButton(
                        onClick = onMessengerClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("messenger_nav_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Messenger",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Notifications Icon with BadgedBox
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(text = if (unreadNotifications > 9) "9+" else unreadNotifications.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = onNotificationsClick,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("notifications_nav_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Profile avatar button
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .testTag("profile_avatar_top_button")
                    ) {
                        FrendoAvatar(
                            avatarResName = userProfile.avatarRes,
                            size = 38.dp,
                            showOnlineBadge = true,
                            isOnline = true
                        )
                    }
                }
            }

            // Search Bar in Top Section
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Search Frendo...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(48.dp)
                    .testTag("frendo_search_input")
            )
        }
    }
}

@Composable
fun StoriesCarousel(
    stories: List<StoryEntity>,
    userProfile: UserProfile,
    onCreateStoryClick: () -> Unit,
    onStoryClick: (StoryEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "Stories",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                // First card: Add Story card
                item {
                    AddStoryCard(
                        userProfile = userProfile,
                        onClick = onCreateStoryClick
                    )
                }

                items(stories) { story ->
                    StoryCard(
                        story = story,
                        onClick = { onStoryClick(story) }
                    )
                }
            }
        }
    }
}

@Composable
fun AddStoryCard(
    userProfile: UserProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        onClick = onClick,
        modifier = modifier
            .width(105.dp)
            .height(160.dp)
            .testTag("add_story_card")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.68f)
            ) {
                AsyncImage(
                    model = R.drawable.img_avatar_alex,
                    contentDescription = "Your avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.32f)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(FrendoBlue)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Story",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Create Story",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun StoryCard(
    story: StoryEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(story.gradientStartHex),
            Color(story.gradientEndHex)
        )
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        border = if (!story.isViewed) BorderStroke(2.dp, FrendoBlue) else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        onClick = onClick,
        modifier = modifier
            .width(105.dp)
            .height(160.dp)
            .testTag("story_card_${story.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            if (story.imageResName != null) {
                val imageRes = when (story.imageResName) {
                    "img_cover_nature" -> R.drawable.img_cover_nature
                    "img_post_desk" -> R.drawable.img_post_desk
                    else -> R.drawable.img_cover_nature
                }
                AsyncImage(
                    model = imageRes,
                    contentDescription = "Story image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
            }

            // Top Avatar
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                FrendoAvatar(
                    avatarResName = story.authorAvatarRes,
                    size = 32.dp,
                    showOnlineBadge = false
                )
            }

            // Story Text
            if (!story.textCaption.isNullOrBlank()) {
                Text(
                    text = story.textCaption,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp)
                )
            }

            // Bottom Name
            Text(
                text = story.authorName,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun PostComposerCard(
    userProfile: UserProfile,
    onComposerClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onFeelingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FrendoAvatar(
                    avatarResName = userProfile.avatarRes,
                    size = 40.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    onClick = onComposerClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("post_composer_open_button")
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "What's on your mind, ${userProfile.name.split(" ").firstOrNull() ?: "Alex"}?",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ComposerActionButton(
                    icon = Icons.Default.Videocam,
                    iconTint = Color(0xFFEF4444),
                    label = "Live",
                    onClick = onComposerClick
                )

                ComposerActionButton(
                    icon = Icons.Default.Image,
                    iconTint = Color(0xFF22C55E),
                    label = "Photo",
                    onClick = onPhotoClick
                )

                ComposerActionButton(
                    icon = Icons.Default.EmojiEmotions,
                    iconTint = Color(0xFFF59E0B),
                    label = "Feeling",
                    onClick = onFeelingClick
                )
            }
        }
    }
}

@Composable
fun ComposerActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: PostEntity,
    onReactionSelect: (String) -> Unit,
    onCommentsClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isCurrentUserPost: Boolean,
    modifier: Modifier = Modifier
) {
    var showReactionPicker by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Author Avatar, Name, timestamp, privacy, more options
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FrendoAvatar(
                    avatarResName = post.authorAvatarRes,
                    size = 42.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = post.authorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (!post.groupName.isNullOrBlank()) {
                            Text(
                                text = "▸ ${post.groupName}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatTimeAgo(post.timestamp),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "•",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = when (post.privacy) {
                                "Friends" -> Icons.Default.People
                                "Only Me" -> Icons.Default.Lock
                                else -> Icons.Default.Public
                            },
                            contentDescription = post.privacy,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )

                        if (!post.feeling.isNullOrBlank()) {
                            Text(
                                text = "• ${post.feeling}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // More Menu
                Box {
                    IconButton(
                        onClick = { showMoreMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Post options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = { showMoreMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (post.isSaved) "Unsave Post" else "Save Post") },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onSaveClick()
                                showMoreMenu = false
                            }
                        )

                        if (isCurrentUserPost) {
                            DropdownMenuItem(
                                text = { Text("Delete Post", color = Color.Red) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                },
                                onClick = {
                                    onDeleteClick()
                                    showMoreMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Post Content Text
            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            // Post Image (if present)
            if (!post.imageResName.isNullOrBlank()) {
                val imageRes = when (post.imageResName) {
                    "img_post_desk" -> R.drawable.img_post_desk
                    "img_cover_nature" -> R.drawable.img_cover_nature
                    else -> R.drawable.img_post_desk
                }

                AsyncImage(
                    model = imageRes,
                    contentDescription = "Post media",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Stats row (Reactions & Comments count)
            if (post.totalReactions > 0 || post.commentsCount > 0 || post.sharesCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reactions badges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ReactionIconsSummary(post = post)
                        Text(
                            text = "${post.totalReactions}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Comments & Shares count
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (post.commentsCount > 0) {
                            Text(
                                text = "${post.commentsCount} comments",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (post.sharesCount > 0) {
                            Text(
                                text = "${post.sharesCount} shares",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Reaction Picker overlay popup
            AnimatedVisibility(
                visible = showReactionPicker,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ReactionPicker(
                    onSelect = { reaction ->
                        onReactionSelect(reaction)
                        showReactionPicker = false
                    },
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Actions Bar: Like / React, Comment, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like / React Button (Support click to toggle default like or long press for picker)
                val currentReaction = post.userReaction
                val reactionColor = when (currentReaction) {
                    "LOVE" -> ReactionLoveRed
                    "LIKE" -> ReactionLikeBlue
                    "HAHA", "WOW", "SAD" -> ReactionHahaYellow
                    "ANGRY" -> ReactionAngryOrange
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                val reactionLabel = when (currentReaction) {
                    "LOVE" -> "Love"
                    "HAHA" -> "Haha"
                    "WOW" -> "Wow"
                    "SAD" -> "Sad"
                    "ANGRY" -> "Angry"
                    "LIKE" -> "Like"
                    else -> "Like"
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .combinedClickable(
                            onClick = {
                                if (showReactionPicker) {
                                    showReactionPicker = false
                                } else {
                                    onReactionSelect(if (currentReaction == null) "LIKE" else currentReaction)
                                }
                            },
                            onLongClick = {
                                showReactionPicker = !showReactionPicker
                            }
                        )
                        .padding(vertical = 6.dp)
                        .testTag("reaction_button_${post.id}")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (currentReaction) {
                                "LOVE" -> "❤️"
                                "HAHA" -> "😂"
                                "WOW" -> "😮"
                                "SAD" -> "😢"
                                "ANGRY" -> "😡"
                                "LIKE" -> "👍"
                                else -> "👍"
                            },
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = reactionLabel,
                            fontWeight = if (currentReaction != null) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = reactionColor
                        )
                    }
                }

                // Comment Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent,
                    onClick = onCommentsClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp)
                        .testTag("comment_button_${post.id}")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Comment",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Share Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent,
                    onClick = onShareClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp)
                        .testTag("share_button_${post.id}")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReactionIconsSummary(post: PostEntity) {
    Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
        if (post.likeCount > 0) {
            Text(text = "👍", fontSize = 14.sp)
        }
        if (post.loveCount > 0) {
            Text(text = "❤️", fontSize = 14.sp)
        }
        if (post.hahaCount > 0) {
            Text(text = "😂", fontSize = 14.sp)
        }
        if (post.wowCount > 0) {
            Text(text = "😮", fontSize = 14.sp)
        }
        if (post.sadCount > 0) {
            Text(text = "😢", fontSize = 14.sp)
        }
        if (post.angryCount > 0) {
            Text(text = "😡", fontSize = 14.sp)
        }
    }
}

@Composable
fun ReactionPicker(
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        tonalElevation = 6.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            REACTION_OPTIONS.forEach { reaction ->
                IconButton(
                    onClick = { onSelect(reaction.type) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Text(
                        text = reaction.emoji,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    postId: Long,
    comments: List<CommentEntity>,
    onDismiss: () -> Unit,
    onAddComment: (String) -> Unit,
    onToggleLike: (Long) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var commentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            HorizontalDivider()

            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Be the first to comment on this post! 💬",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(280.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    items(comments) { comment ->
                        CommentItem(
                            comment = comment,
                            onLikeClick = { onToggleLike(comment.id) }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Comment input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FrendoAvatar(
                    avatarResName = "img_avatar_alex",
                    size = 36.dp
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Write a comment...", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("comment_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onAddComment(commentText)
                            commentText = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (commentText.isNotBlank()) FrendoBlue else Color.LightGray)
                        .testTag("send_comment_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: CommentEntity,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        FrendoAvatar(
            avatarResName = comment.authorAvatarRes,
            size = 34.dp
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = comment.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = comment.text,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimeAgo(comment.timestamp),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (comment.isLiked) "Liked" else "Like",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (comment.isLiked) FrendoBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable(onClick = onLikeClick)
                )

                if (comment.likeCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "👍", fontSize = 11.sp)
                        Text(
                            text = "${comment.likeCount}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryViewerDialog(
    story: StoryEntity,
    onDismiss: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(story.id) {
        progress = 0f
        val steps = 100
        for (i in 1..steps) {
            delay(50) // 5 seconds total
            progress = i / steps.toFloat()
        }
        onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(story.gradientStartHex), Color(story.gradientEndHex))
                    )
                )
        ) {
            if (story.imageResName != null) {
                val imageRes = when (story.imageResName) {
                    "img_cover_nature" -> R.drawable.img_cover_nature
                    "img_post_desk" -> R.drawable.img_post_desk
                    else -> R.drawable.img_cover_nature
                }
                AsyncImage(
                    model = imageRes,
                    contentDescription = "Story full image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Progress & Author info
                Column {
                    LinearProgressIndicator(
                        progress = { progress },
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FrendoAvatar(avatarResName = story.authorAvatarRes, size = 38.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = story.authorName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = formatTimeAgo(story.timestamp),
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center Caption
                if (!story.textCaption.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = story.textCaption,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 28.sp
                        )
                    }
                }

                // Bottom Reply Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color.Black.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Reply to ${story.authorName.split(" ").firstOrNull()}...",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.4f))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "❤️", fontSize = 20.sp)
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.4f))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔥", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m"
        hours < 24 -> "${hours}h"
        days < 7 -> "${days}d"
        else -> "${days / 7}w"
    }
}
