package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FriendEntity
import com.example.ui.components.FrendoAvatar
import com.example.ui.theme.FrendoBlue

@Composable
fun FriendsScreen(
    friends: List<FriendEntity>,
    onAcceptRequest: (Long) -> Unit,
    onDeclineRequest: (Long) -> Unit,
    onSendRequest: (Long) -> Unit,
    onUnfriend: (Long) -> Unit,
    onOpenChat: (FriendEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All Friends", "Friend Requests", "Suggestions")

    val requests = friends.filter { it.status == "REQUEST_RECEIVED" }
    val accepted = friends.filter { it.status == "ACCEPTED" }
    val suggestions = friends.filter { it.status == "SUGGESTION" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedSubTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                val countText = when (index) {
                    0 -> " (${accepted.size})"
                    1 -> if (requests.isNotEmpty()) " (${requests.size})" else ""
                    2 -> " (${suggestions.size})"
                    else -> ""
                }
                Tab(
                    selected = selectedSubTabIndex == index,
                    onClick = { selectedSubTabIndex = index },
                    text = {
                        Text(
                            text = "$title$countText",
                            fontWeight = if (selectedSubTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            when (selectedSubTabIndex) {
                0 -> { // All Friends
                    if (accepted.isEmpty()) {
                        item {
                            EmptyFriendsState("No friends yet. Check out suggestions!")
                        }
                    } else {
                        items(accepted, key = { it.id }) { friend ->
                            AcceptedFriendCard(
                                friend = friend,
                                onOpenChat = { onOpenChat(friend) },
                                onUnfriend = { onUnfriend(friend.id) }
                            )
                        }
                    }
                }
                1 -> { // Requests
                    if (requests.isEmpty()) {
                        item {
                            EmptyFriendsState("No pending friend requests.")
                        }
                    } else {
                        items(requests, key = { it.id }) { friend ->
                            FriendRequestCard(
                                friend = friend,
                                onAccept = { onAcceptRequest(friend.id) },
                                onDecline = { onDeclineRequest(friend.id) }
                            )
                        }
                    }
                }
                2 -> { // Suggestions
                    if (suggestions.isEmpty()) {
                        item {
                            EmptyFriendsState("No friend suggestions at the moment.")
                        }
                    } else {
                        items(suggestions, key = { it.id }) { friend ->
                            SuggestionCard(
                                friend = friend,
                                onAddFriend = { onSendRequest(friend.id) },
                                onRemove = { onDeclineRequest(friend.id) }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AcceptedFriendCard(
    friend: FriendEntity,
    onOpenChat: () -> Unit,
    onUnfriend: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FrendoAvatar(
                avatarResName = friend.avatarRes,
                size = 50.dp,
                showOnlineBadge = true,
                isOnline = friend.isOnline
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (friend.isOnline) "Active now" else friend.lastActiveText,
                    fontSize = 12.sp,
                    color = if (friend.isOnline) Color(0xFF22C55E) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (friend.mutualFriends > 0) {
                    Text(
                        text = "${friend.mutualFriends} mutual friends",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onOpenChat,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                    modifier = Modifier.testTag("message_friend_${friend.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Message",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Message", fontSize = 13.sp, color = Color.White)
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Unfriend ${friend.name}", color = Color.Red) },
                            onClick = {
                                onUnfriend()
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FriendRequestCard(
    friend: FriendEntity,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FrendoAvatar(avatarResName = friend.avatarRes, size = 52.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (friend.mutualFriends > 0) {
                    Text(
                        text = "${friend.mutualFriends} mutual friends",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (friend.bio.isNotBlank()) {
                    Text(
                        text = friend.bio,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onAccept,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                        modifier = Modifier.testTag("confirm_friend_${friend.id}")
                    ) {
                        Text(text = "Confirm", fontSize = 13.sp, color = Color.White)
                    }

                    Button(
                        onClick = onDecline,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.testTag("delete_request_${friend.id}")
                    ) {
                        Text(
                            text = "Delete",
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
fun SuggestionCard(
    friend: FriendEntity,
    onAddFriend: () -> Unit,
    onRemove: () -> Unit
) {
    var isSent by remember { mutableStateOf(friend.status == "REQUEST_SENT") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FrendoAvatar(avatarResName = friend.avatarRes, size = 52.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (friend.mutualFriends > 0) {
                    Text(
                        text = "${friend.mutualFriends} mutual friends",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (friend.bio.isNotBlank()) {
                    Text(
                        text = friend.bio,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            isSent = true
                            onAddFriend()
                        },
                        enabled = !isSent,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSent) Color.Gray else FrendoBlue
                        ),
                        modifier = Modifier.testTag("add_friend_${friend.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSent) "Request Sent" else "Add Friend",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    if (!isSent) {
                        Button(
                            onClick = onRemove,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "Remove",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFriendsState(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
