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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.ui.components.FrendoAvatar
import com.example.ui.components.formatTimeAgo
import com.example.ui.theme.FrendoBlue

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity>,
    onMarkAllRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextButton(onClick = onMarkAllRead) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = null,
                    tint = FrendoBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Mark all as read",
                    color = FrendoBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You're all caught up! No notifications yet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(notifications, key = { it.id }) { item ->
                    NotificationItemCard(notification = item)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationItemCard(notification: NotificationEntity) {
    val icon = when (notification.type) {
        "REACTION" -> Icons.Default.Favorite
        "COMMENT" -> Icons.Default.ChatBubbleOutline
        "FRIEND_REQUEST" -> Icons.Default.PersonAdd
        "GROUP" -> Icons.Default.Groups
        else -> Icons.Default.Notifications
    }

    val iconBgColor = when (notification.type) {
        "REACTION" -> Color(0xFFEF4444)
        "COMMENT" -> Color(0xFF22C55E)
        "FRIEND_REQUEST" -> FrendoBlue
        "GROUP" -> Color(0xFF7C3AED)
        else -> FrendoBlue
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) {
                FrendoBlue.copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
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
            Box {
                FrendoAvatar(avatarResName = notification.actorAvatarRes, size = 48.dp)
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(iconBgColor)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${notification.actorName} ${notification.contentText}",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTimeAgo(notification.timestamp),
                    fontSize = 11.sp,
                    color = if (!notification.isRead) FrendoBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (!notification.isRead) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(FrendoBlue)
                )
            }
        }
    }
}
