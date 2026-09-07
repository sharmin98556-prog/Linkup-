package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FriendEntity
import com.example.data.model.MessageEntity
import com.example.ui.components.FrendoAvatar
import com.example.ui.components.formatTimeAgo
import com.example.ui.theme.FrendoAccentGreen
import com.example.ui.theme.FrendoBlue

@Composable
fun MessengerScreen(
    friends: List<FriendEntity>,
    activeChatFriend: FriendEntity?,
    messages: List<MessageEntity>,
    onOpenChat: (FriendEntity) -> Unit,
    onCloseChat: () -> Unit,
    onSendMessage: (FriendEntity, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeChatFriend != null) {
        ActiveChatView(
            friend = activeChatFriend,
            messages = messages,
            onBack = onCloseChat,
            onSendMessage = { text -> onSendMessage(activeChatFriend, text) },
            modifier = modifier
        )
    } else {
        ConversationsListView(
            friends = friends,
            onOpenChat = onOpenChat,
            modifier = modifier
        )
    }
}

@Composable
fun ConversationsListView(
    friends: List<FriendEntity>,
    onOpenChat: (FriendEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val onlineFriends = friends.filter { it.isOnline }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Active Friends Horizontal Reel
        if (onlineFriends.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Active Friends",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(onlineFriends) { friend ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { onOpenChat(friend) }
                            ) {
                                FrendoAvatar(
                                    avatarResName = friend.avatarRes,
                                    size = 48.dp,
                                    showOnlineBadge = true,
                                    isOnline = true
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = friend.name.split(" ").firstOrNull() ?: "",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Messages",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(friends) { friend ->
                ConversationItem(
                    friend = friend,
                    onClick = { onOpenChat(friend) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ConversationItem(
    friend: FriendEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("conversation_item_${friend.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FrendoAvatar(
                avatarResName = friend.avatarRes,
                size = 52.dp,
                showOnlineBadge = true,
                isOnline = friend.isOnline
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = friend.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (friend.isOnline) "Active" else "1h ago",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Tap to view conversation with ${friend.name.split(" ").firstOrNull()}...",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ActiveChatView(
    friend: FriendEntity,
    messages: List<MessageEntity>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Chat Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                FrendoAvatar(
                    avatarResName = friend.avatarRes,
                    size = 40.dp,
                    showOnlineBadge = true,
                    isOnline = friend.isOnline
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = friend.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (friend.isOnline) "Active now" else friend.lastActiveText,
                        fontSize = 11.sp,
                        color = if (friend.isOnline) FrendoAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Audio Call",
                        tint = FrendoBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = FrendoBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = false
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            FrendoAvatar(avatarResName = friend.avatarRes, size = 64.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = friend.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "You're connected on Frendo. Say hi! 👋",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }
        }

        // Bottom Composer
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Type a message...", fontSize = 14.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = FrendoBlue,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("chat_text_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(textInput)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (textInput.isNotBlank()) FrendoBlue else Color.LightGray)
                        .testTag("chat_send_button")
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
fun ChatBubble(message: MessageEntity) {
    val isMe = message.isFromMe

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            color = if (isMe) FrendoBlue else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatTimeAgo(message.timestamp),
                    fontSize = 10.sp,
                    color = if (isMe) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(if (isMe) Alignment.End else Alignment.Start)
                )
            }
        }
    }
}
