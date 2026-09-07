package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserProfile
import com.example.ui.theme.FrendoBlue

@Composable
fun CreatePostDialog(
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onPostCreated: (content: String, imageRes: String?, privacy: String, feeling: String?) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf("Public") }
    var selectedImageRes by remember { mutableStateOf<String?>(null) }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }

    var showPrivacyMenu by remember { mutableStateOf(false) }
    var showFeelingMenu by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Post",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Author Info + Privacy pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FrendoAvatar(avatarResName = userProfile.avatarRes, size = 44.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = userProfile.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Privacy selector button
                        Box {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = { showPrivacyMenu = true },
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = when (privacy) {
                                            "Friends" -> Icons.Default.People
                                            "Only Me" -> Icons.Default.Lock
                                            else -> Icons.Default.Public
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = privacy,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(text = "▾", fontSize = 10.sp)
                                }
                            }

                            DropdownMenu(
                                expanded = showPrivacyMenu,
                                onDismissRequest = { showPrivacyMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Public (Anyone on Frendo)") },
                                    leadingIcon = { Icon(Icons.Default.Public, null) },
                                    onClick = { privacy = "Public"; showPrivacyMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Friends Only") },
                                    leadingIcon = { Icon(Icons.Default.People, null) },
                                    onClick = { privacy = "Friends"; showPrivacyMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Only Me") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                                    onClick = { privacy = "Only Me"; showPrivacyMenu = false }
                                )
                            }
                        }
                    }
                }

                if (selectedFeeling != null) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedFeeling!!, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove feeling",
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { selectedFeeling = null }
                        )
                    }
                }

                // Text Field Input
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = {
                        Text(
                            "What's on your mind, ${userProfile.name.split(" ").firstOrNull()}?",
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(vertical = 10.dp)
                        .testTag("create_post_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Add to your post options (Photos / Feelings)
                Card(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add to your post",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Photo attachment toggle
                            IconButton(
                                onClick = {
                                    selectedImageRes = if (selectedImageRes == null) "img_post_desk" else null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Attach Photo",
                                    tint = if (selectedImageRes != null) FrendoBlue else Color(0xFF22C55E)
                                )
                            }

                            // Feeling menu
                            Box {
                                IconButton(onClick = { showFeelingMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Mood,
                                        contentDescription = "Add Feeling",
                                        tint = Color(0xFFF59E0B)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showFeelingMenu,
                                    onDismissRequest = { showFeelingMenu = false }
                                ) {
                                    listOf(
                                        "☕ feeling productive",
                                        "😊 feeling happy",
                                        "✨ feeling inspired",
                                        "🎉 celebrating",
                                        "🚀 building cool stuff",
                                        "🎧 listening to music"
                                    ).forEach { feel ->
                                        DropdownMenuItem(
                                            text = { Text(feel) },
                                            onClick = {
                                                selectedFeeling = feel
                                                showFeelingMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (selectedImageRes != null) {
                    Text(
                        text = "✓ Photo attached (Modern desk workspace)",
                        fontSize = 12.sp,
                        color = Color(0xFF22C55E),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Post Button
                Button(
                    onClick = {
                        onPostCreated(content, selectedImageRes, privacy, selectedFeeling)
                    },
                    enabled = content.isNotBlank() || selectedImageRes != null,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("submit_post_button")
                ) {
                    Text(
                        text = "Post",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CreateStoryDialog(
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onStoryCreated: (caption: String?, imageRes: String?, startColor: Long, endColor: Long) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var includePhoto by remember { mutableStateOf(false) }

    val gradients = listOf(
        Pair(0xFF2563EBL, 0xFF7C3AEDL), // Blue -> Purple
        Pair(0xFF059669L, 0xFF10B981L), // Emerald -> Mint
        Pair(0xFFDB2777L, 0xFFF43F5EL), // Pink -> Rose
        Pair(0xFFD97706L, 0xFFF59E0BL), // Amber -> Orange
        Pair(0xFF1E293BL, 0xFF475569L)  // Dark Slate
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add to Story",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Preview Card
                val (startCol, endCol) = gradients[selectedColorIndex]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(startCol), Color(endCol))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (caption.isNotBlank()) caption else "Your story text preview here...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Caption Input
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = { Text("Start typing your story...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Gradient selector
                Text(
                    text = "Pick Background Gradient",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(gradients.indices.toList()) { index ->
                        val (s, e) = gradients[index]
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(s), Color(e))
                                    )
                                )
                                .border(
                                    width = if (selectedColorIndex == index) 3.dp else 1.dp,
                                    color = if (selectedColorIndex == index) FrendoBlue else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorIndex = index }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Share Button
                Button(
                    onClick = {
                        val (s, e) = gradients[selectedColorIndex]
                        val img = if (includePhoto) "img_post_desk" else null
                        onStoryCreated(caption.ifBlank { "Sharing moments ✨" }, img, s, e)
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("submit_story_button")
                ) {
                    Text(text = "Share to Story", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (name: String, bio: String, location: String, work: String) -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var bio by remember { mutableStateOf(userProfile.bio) }
    var location by remember { mutableStateOf(userProfile.location) }
    var work by remember { mutableStateOf(userProfile.work) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = work,
                    onValueChange = { work = it },
                    label = { Text("Work / Occupation") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(name, bio, location, work) },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(text = "Save Profile", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Change Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Current Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    label = { Text("Confirm New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newPass.length < 6) {
                                errorMessage = "Password must be at least 6 characters"
                            } else if (newPass != confirmPass) {
                                errorMessage = "Passwords do not match"
                            } else {
                                onSuccess()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue)
                    ) {
                        Text("Update Password", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Deactivate / Delete Account",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to deactivate your Frendo account? Your profile, posts, stories, and friends connections will be archived. You can reactivate anytime by logging back in.",
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Deactivate Account", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
