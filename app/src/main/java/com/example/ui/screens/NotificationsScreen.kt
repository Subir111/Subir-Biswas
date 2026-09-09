package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.repository.SubirRepository
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirOrange
import com.example.ui.theme.SubirPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onUserProfileClick: (String) -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val notifications by SubirRepository.notifications.collectAsState()
    var selectedFilter by remember { mutableStateOf(0) } // 0: All, 1: Requests, 2: Mentions

    val filters = listOf(
        SubirStrings.get("all", lang),
        SubirStrings.get("friend_requests", lang),
        "Mentions"
    )

    val filteredNotifications = when (selectedFilter) {
        1 -> notifications.filter { it.type == NotificationType.FRIEND_REQUEST || it.type == NotificationType.FRIEND_ACCEPTED }
        2 -> notifications.filter { it.type == NotificationType.MENTION }
        else -> notifications
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = SubirStrings.get("notifications", lang),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = { SubirRepository.markNotificationsRead() }) {
                        Text(SubirStrings.get("mark_all_read", lang), fontSize = 13.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("notifications_screen")
        ) {
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEachIndexed { index, label ->
                    FilterChip(
                        selected = selectedFilter == index,
                        onClick = { selectedFilter = index },
                        label = { Text(label) },
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (filteredNotifications.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No notifications right now",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredNotifications, key = { it.id }) { notif ->
                        NotificationRow(
                            item = notif,
                            onUserClick = { onUserProfileClick(notif.actor.id) },
                            onAcceptFriend = { SubirRepository.acceptFriendRequest(notif.actor.id) },
                            onRejectFriend = { SubirRepository.rejectFriendRequest(notif.actor.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    item: NotificationItem,
    onUserClick: () -> Unit,
    onAcceptFriend: () -> Unit,
    onRejectFriend: () -> Unit
) {
    val iconAndColor = when (item.type) {
        NotificationType.LOVE -> "❤️" to SubirMagenta
        NotificationType.LIKE -> "👍" to SubirCyan
        NotificationType.COMMENT -> "💬" to SubirPurple
        NotificationType.SHARE -> "🔁" to Color(0xFF4CAF50)
        NotificationType.FOLLOW -> "👤" to SubirOrange
        NotificationType.FRIEND_REQUEST -> "🤝" to SubirCyan
        NotificationType.FRIEND_ACCEPTED -> "🎉" to Color(0xFF4CAF50)
        NotificationType.MENTION -> "📢" to SubirOrange
        else -> "🔔" to SubirCyan
    }

    Surface(
        color = if (item.isRead) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = item.actor.avatarUrl,
                    contentDescription = item.actor.name,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = iconAndColor.first,
                    fontSize = 14.sp,
                    modifier = Modifier.offset(x = 4.dp, y = 4.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.actor.name} ${item.message}",
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "15m ago",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Inline Actions for Friend Requests
                if (item.type == NotificationType.FRIEND_REQUEST) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onAcceptFriend,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Confirm", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onRejectFriend,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Delete", fontSize = 12.sp)
                        }
                    }
                }
            }

            if (!item.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
