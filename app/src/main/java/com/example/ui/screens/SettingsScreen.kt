package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.locale.AppLanguage
import com.example.data.locale.SubirStrings
import com.example.data.model.UserRole
import com.example.data.repository.SubirRepository
import com.example.data.repository.ThemeMode
import com.example.ui.components.SubirLogoImage
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirOrange
import com.example.ui.theme.SubirPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onLogout: () -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val themeMode by SubirRepository.themeMode.collectAsState()
    val currentUser by SubirRepository.currentUser.collectAsState()
    val context = LocalContext.current

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showBlockedUsersDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val isAdmin = currentUser?.role == UserRole.ADMIN || currentUser?.role == UserRole.SUPER_ADMIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(SubirStrings.get("settings", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("settings_screen"),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Preferences Section (Language & Theme)
            SettingsSectionHeader(title = "Preferences")

            // Language Switcher
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = SubirCyan)
                            Text(text = SubirStrings.get("language", lang), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = lang == AppLanguage.ENGLISH,
                            onClick = { SubirRepository.setLanguage(AppLanguage.ENGLISH) },
                            label = { Text("English") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = lang == AppLanguage.BENGALI,
                            onClick = { SubirRepository.setLanguage(AppLanguage.BENGALI) },
                            label = { Text("বাংলা (Bengali)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Theme Mode Switcher (Light / Dark / System)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = SubirPurple)
                        Text(text = SubirStrings.get("theme", lang), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = themeMode == ThemeMode.LIGHT,
                            onClick = { SubirRepository.setThemeMode(ThemeMode.LIGHT) },
                            label = { Text(SubirStrings.get("light", lang), fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = themeMode == ThemeMode.DARK,
                            onClick = { SubirRepository.setThemeMode(ThemeMode.DARK) },
                            label = { Text(SubirStrings.get("dark", lang), fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = themeMode == ThemeMode.SYSTEM,
                            onClick = { SubirRepository.setThemeMode(ThemeMode.SYSTEM) },
                            label = { Text(SubirStrings.get("system_default", lang), fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Admin Panel Shortcut (if admin)
            if (isAdmin) {
                SettingsSectionHeader(title = "Administration")
                SettingsActionTile(
                    icon = Icons.Default.AdminPanelSettings,
                    iconColor = SubirMagenta,
                    title = "Subir Admin & Moderation Panel",
                    subtitle = "Manage users, reports, announcements & verification",
                    onClick = onNavigateToAdmin
                )
            }

            // Privacy & Security Section
            SettingsSectionHeader(title = SubirStrings.get("privacy", lang) + " & Security")

            SettingsActionTile(
                icon = Icons.Default.Lock,
                iconColor = SubirCyan,
                title = "Account Privacy",
                subtitle = "Public profile, active status, message requests",
                onClick = { Toast.makeText(context, "Privacy settings saved", Toast.LENGTH_SHORT).show() }
            )

            SettingsActionTile(
                icon = Icons.Default.Block,
                iconColor = SubirOrange,
                title = SubirStrings.get("blocked_users", lang),
                subtitle = "Manage blocked accounts",
                onClick = { showBlockedUsersDialog = true }
            )

            SettingsActionTile(
                icon = Icons.Default.Security,
                iconColor = Color(0xFF4CAF50),
                title = "Two-Factor Authentication",
                subtitle = "Protect your account with OTP security",
                onClick = { Toast.makeText(context, "Two-factor authentication is active", Toast.LENGTH_SHORT).show() }
            )

            // Legal & About
            SettingsSectionHeader(title = "About & Community")

            SettingsActionTile(
                icon = Icons.Default.Info,
                iconColor = SubirCyan,
                title = "About Subir",
                subtitle = "Version 1.0.0 (Production) • Made with ❤️ in Bangladesh",
                onClick = { showAboutDialog = true }
            )

            SettingsActionTile(
                icon = Icons.Default.Gavel,
                iconColor = SubirPurple,
                title = "Terms & Community Guidelines",
                subtitle = "Safety policies, user rights & standards",
                onClick = { Toast.makeText(context, "Opening Subir Community Guidelines", Toast.LENGTH_SHORT).show() }
            )

            // Account Actions (Logout / Delete)
            SettingsSectionHeader(title = "Account Actions")

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = SubirStrings.get("logout", lang),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = { showDeleteAccountDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Delete Account",
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
        }
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(SubirStrings.get("logout", lang)) },
            text = { Text("Are you sure you want to log out of Subir?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        SubirRepository.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Account Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = { Text("Delete Account permanently?") },
            text = { Text("This will erase all your posts, reels, stories and profile data from Subir servers. This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        SubirRepository.logout()
                        onLogout()
                        Toast.makeText(context, "Account deleted", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Permanently")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Blocked Users Dialog
    if (showBlockedUsersDialog) {
        val blockedList by SubirRepository.blockedUsers.collectAsState()
        AlertDialog(
            onDismissRequest = { showBlockedUsersDialog = false },
            title = { Text(SubirStrings.get("blocked_users", lang)) },
            text = {
                if (blockedList.isEmpty()) {
                    Text("You have not blocked any users.", fontSize = 14.sp)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        blockedList.forEach { user ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = user.name, fontSize = 14.sp)
                                TextButton(onClick = { SubirRepository.unblockUser(user.id) }) {
                                    Text("Unblock")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBlockedUsersDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SubirLogoImage(size = 32.dp)
                    Text("About Subir")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Subir is a high-performance next-generation social media platform designed for vibrant storytelling, reels, community engagement, and digital empowerment.", fontSize = 14.sp)
                    Text("• Bilingual Support: English + বাংলা\n• Jetpack Compose & Kotlin\n• Full creator monetization and analytics\n• Original Bangladeshi branding", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("Awesome")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp
    )
}

@Composable
private fun SettingsActionTile(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
