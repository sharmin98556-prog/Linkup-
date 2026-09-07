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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettingsEntity
import com.example.ui.theme.FrendoBlue

@Composable
fun SettingsScreen(
    appSettings: AppSettingsEntity,
    onGeneralSave: (name: String, email: String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onPrivacySave: (postPrivacy: String, friendReq: String, emailLookup: String) -> Unit,
    onNotificationToggle: (String) -> Unit,
    onBlockUser: (String) -> Unit,
    onUnblockUser: (String) -> Unit,
    onLogoutAllSessions: () -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sections = listOf(
        "General",
        "Privacy",
        "Notifications",
        "Blocking",
        "Security",
        "Theme",
        "Language",
        "Account",
        "Firebase Specs"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Settings & Privacy",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = selectedSection,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = FrendoBlue,
            edgePadding = 16.dp
        ) {
            sections.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSection == index,
                    onClick = { selectedSection = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                when (selectedSection) {
                    0 -> GeneralSettingsView(
                        settings = appSettings,
                        onSave = onGeneralSave,
                        onChangePasswordClick = onChangePasswordClick
                    )
                    1 -> PrivacySettingsView(
                        settings = appSettings,
                        onSave = onPrivacySave
                    )
                    2 -> NotificationSettingsView(
                        settings = appSettings,
                        onToggle = onNotificationToggle
                    )
                    3 -> BlockingSettingsView(
                        settings = appSettings,
                        onBlockUser = onBlockUser,
                        onUnblockUser = onUnblockUser
                    )
                    4 -> SecuritySettingsView(
                        settings = appSettings,
                        onLogoutSessions = onLogoutAllSessions
                    )
                    5 -> ThemeSettingsView(
                        settings = appSettings,
                        onThemeToggle = onThemeToggle
                    )
                    6 -> LanguageSettingsView(
                        settings = appSettings,
                        onLanguageChange = onLanguageChange
                    )
                    7 -> AccountSettingsView(
                        onDeleteClick = onDeleteAccountClick
                    )
                    8 -> FirebaseRulesView()
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun GeneralSettingsView(
    settings: AppSettingsEntity,
    onSave: (String, String) -> Unit,
    onChangePasswordClick: () -> Unit
) {
    var name by remember { mutableStateOf(settings.userName) }
    var email by remember { mutableStateOf(settings.userEmail) }
    var savedFeedback by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "General Account Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; savedFeedback = false },
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; savedFeedback = false },
                label = { Text("Email Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (savedFeedback) {
                Text(
                    text = "✓ Changes saved successfully!",
                    color = Color(0xFF22C55E),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onChangePasswordClick) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Change Password")
                }

                Button(
                    onClick = {
                        onSave(name, email)
                        savedFeedback = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue)
                ) {
                    Text("Save Changes", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsView(
    settings: AppSettingsEntity,
    onSave: (String, String, String) -> Unit
) {
    var postPrivacy by remember { mutableStateOf(settings.postPrivacy) }
    var friendReq by remember { mutableStateOf(settings.friendRequestPermission) }
    var emailLookup by remember { mutableStateOf(settings.emailLookupPermission) }
    var saved by remember { mutableStateOf(false) }

    val postPrivacyOptions = listOf("Public", "Friends", "Only Me")
    val friendReqOptions = listOf("Everyone", "Friends of Friends")
    val lookupOptions = listOf("Everyone", "Friends")

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Privacy & Audience Controls",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Post Privacy Dropdown
            Text(
                text = "Who can see your future posts?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            DropdownSelector(
                selected = postPrivacy,
                options = postPrivacyOptions,
                onSelected = { postPrivacy = it; saved = false }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Friend Request Dropdown
            Text(
                text = "Who can send you friend requests?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            DropdownSelector(
                selected = friendReq,
                options = friendReqOptions,
                onSelected = { friendReq = it; saved = false }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Lookup Dropdown
            Text(
                text = "Who can look you up using your email?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            DropdownSelector(
                selected = emailLookup,
                options = lookupOptions,
                onSelected = { emailLookup = it; saved = false }
            )

            if (saved) {
                Text(
                    text = "✓ Privacy preferences updated!",
                    color = Color(0xFF22C55E),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSave(postPrivacy, friendReq, emailLookup)
                    saved = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = FrendoBlue),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Privacy Settings", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationSettingsView(
    settings: AppSettingsEntity,
    onToggle: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Notification Preferences",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            SettingSwitchRow(
                title = "Push Notifications",
                description = "Receive instant push alerts for reactions, comments, and messages",
                checked = settings.pushNotifications,
                onCheckedChange = { onToggle("push") }
            )

            SettingSwitchRow(
                title = "Email Notifications",
                description = "Daily digest and important security announcements",
                checked = settings.emailNotifications,
                onCheckedChange = { onToggle("email") }
            )

            SettingSwitchRow(
                title = "In-App Sounds",
                description = "Play notification and reaction sound effects while using the app",
                checked = settings.inAppSounds,
                onCheckedChange = { onToggle("sound") }
            )
        }
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = FrendoBlue)
        )
    }
}

@Composable
fun BlockingSettingsView(
    settings: AppSettingsEntity,
    onBlockUser: (String) -> Unit,
    onUnblockUser: (String) -> Unit
) {
    var blockInput by remember { mutableStateOf("") }
    val blockedList = settings.blockedUsers.split(",").filter { it.isNotBlank() }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Block, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Manage Blocked Users",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "Once you block someone, they won't be able to see your posts, tag you, invite you to groups, or message you.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = blockInput,
                    onValueChange = { blockInput = it },
                    placeholder = { Text("Enter username to block...") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (blockInput.isNotBlank()) {
                            onBlockUser(blockInput)
                            blockInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Block", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Currently Blocked (${blockedList.size}):",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )

            if (blockedList.isEmpty()) {
                Text(
                    text = "You haven't blocked anyone.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                blockedList.forEach { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = user, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        OutlinedButton(
                            onClick = { onUnblockUser(user) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Unblock", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecuritySettingsView(
    settings: AppSettingsEntity,
    onLogoutSessions: () -> Unit
) {
    var loggedOutFeedback by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Security & Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "Where You're Logged In",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Text(
                text = settings.activeSessions,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            if (loggedOutFeedback) {
                Text(
                    text = "✓ All other active sessions have been signed out.",
                    color = Color(0xFF22C55E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    onLogoutSessions()
                    loggedOutFeedback = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Log Out Of All Other Sessions",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ThemeSettingsView(
    settings: AppSettingsEntity,
    onThemeToggle: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DarkMode, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Theme & Appearance",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            SettingSwitchRow(
                title = "Dark Mode",
                description = "Adjust the appearance of Frendo to reduce glare and save battery",
                checked = settings.isDarkMode,
                onCheckedChange = { onThemeToggle(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsView(
    settings: AppSettingsEntity,
    onLanguageChange: (String) -> Unit
) {
    val languages = listOf("English (US)", "Español", "Français", "Deutsch", "日本語", "Português")
    var selectedLang by remember { mutableStateOf(settings.language) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Language & Region",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "Select Application Language",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownSelector(
                selected = selectedLang,
                options = languages,
                onSelected = {
                    selectedLang = it
                    onLanguageChange(it)
                }
            )
        }
    }
}

@Composable
fun AccountSettingsView(
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Account Deactivation & Deletion",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "Temporarily deactivate your profile or permanently delete your account and personal data across Frendo.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.testTag("deactivate_account_button")
            ) {
                Text("Deactivate / Delete Account", color = Color.White)
            }
        }
    }
}

@Composable
fun FirebaseRulesView() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Code, contentDescription = null, tint = FrendoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Firebase Backend & Security Rules",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "Firestore security rules scoped to own data, and Firebase Storage folder structure as specified in project requirements.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Firestore Security Rules:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = """
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    match /posts/{postId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update, delete: if request.auth != null && 
        resource.data.authorId == request.auth.uid;
    }
    match /posts/{postId}/comments/{commentId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow delete: if request.auth != null &&
        resource.data.authorId == request.auth.uid;
    }
    match /messages/{messageId} {
      allow read, write: if request.auth != null &&
        (request.auth.uid in resource.data.participants);
    }
  }
}
                    """.trimIndent(),
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Firebase Storage Rules:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = """
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    match /posts/{postId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
                    """.trimIndent(),
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
