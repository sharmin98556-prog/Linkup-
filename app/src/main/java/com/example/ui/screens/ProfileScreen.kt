package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.PostEntity
import com.example.data.model.UserProfile
import com.example.ui.components.FrendoAvatar
import com.example.ui.components.PostCard
import com.example.ui.theme.FrendoBlue

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    userPosts: List<PostEntity>,
    onAddStoryClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onReactionSelect: (postId: Long, reaction: String) -> Unit,
    onCommentsClick: (postId: Long) -> Unit,
    onShareClick: (postId: Long) -> Unit,
    onSaveClick: (postId: Long) -> Unit,
    onDeleteClick: (postId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val profileTabs = listOf("Posts", "About", "Friends", "Photos")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            ProfileHeaderSection(
                userProfile = userProfile,
                onAddStoryClick = onAddStoryClick,
                onEditProfileClick = onEditProfileClick
            )
        }

        item {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = FrendoBlue,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                profileTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        when (selectedTabIndex) {
            0 -> { // Posts
                if (userPosts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You haven't posted anything yet.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(userPosts, key = { it.id }) { post ->
                        PostCard(
                            post = post,
                            onReactionSelect = { reaction -> onReactionSelect(post.id, reaction) },
                            onCommentsClick = { onCommentsClick(post.id) },
                            onShareClick = { onShareClick(post.id) },
                            onSaveClick = { onSaveClick(post.id) },
                            onDeleteClick = { onDeleteClick(post.id) },
                            isCurrentUserPost = true
                        )
                    }
                }
            }
            1 -> { // About
                item {
                    ProfileAboutSection(userProfile = userProfile)
                }
            }
            2 -> { // Friends
                item {
                    ProfileFriendsSection(userProfile = userProfile)
                }
            }
            3 -> { // Photos
                item {
                    ProfilePhotosSection()
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ProfileHeaderSection(
    userProfile: UserProfile,
    onAddStoryClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Cover Photo with overlap
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.img_cover_nature)
                    .crossfade(true)
                    .build(),
                contentDescription = "Cover photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Edit Cover Button
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                IconButton(
                    onClick = onEditProfileClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Edit cover photo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Profile Avatar & Information
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Overlapping Avatar
            Box(
                modifier = Modifier
                    .offset(y = (-45).dp)
                    .size(100.dp)
            ) {
                AsyncImage(
                    model = R.drawable.img_avatar_alex,
                    contentDescription = "Profile avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                )

                // Camera icon badge
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                ) {
                    IconButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change photo",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // User Info (offset slightly to compensate for overlapping avatar)
            Column(modifier = Modifier.offset(y = (-35).dp)) {
                Text(
                    text = userProfile.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${userProfile.friendsCount} friends • ${userProfile.followersCount} followers",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (userProfile.bio.isNotBlank()) {
                    Text(
                        text = userProfile.bio,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Action Buttons: "+ Add to story" and "Edit profile"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onAddStoryClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("profile_add_story_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add to story",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = onEditProfileClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("profile_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Edit profile",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileAboutSection(userProfile: UserProfile) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            AboutRow(
                icon = Icons.Default.Work,
                label = "Works at",
                value = userProfile.work
            )

            AboutRow(
                icon = Icons.Default.LocationOn,
                label = "Lives in",
                value = userProfile.location
            )

            AboutRow(
                icon = Icons.Default.CalendarToday,
                label = "Joined",
                value = userProfile.joinedDate
            )

            AboutRow(
                icon = Icons.Default.People,
                label = "Friends",
                value = "${userProfile.friendsCount} mutual connections"
            )
        }
    }
}

@Composable
fun AboutRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ProfileFriendsSection(userProfile: UserProfile) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Friends (${userProfile.friendsCount})",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "348 friends connected on Frendo. Switch to the Friends tab above to manage requests and see who's online!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfilePhotosSection() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Photos",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = R.drawable.img_post_desk,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                AsyncImage(
                    model = R.drawable.img_cover_nature,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}
