package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirOrange
import com.example.ui.theme.SubirPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActionSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onCreatePost: () -> Unit,
    onCreateStory: () -> Unit,
    onCreateReel: () -> Unit,
    onUploadPhoto: () -> Unit,
    onUploadVideo: () -> Unit,
    translator: (String) -> String
) {
    if (!isOpen) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = translator("create"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CreateOptionItem(
                icon = Icons.Default.EditNote,
                color = SubirCyan,
                title = translator("post"),
                subtitle = "Share thoughts, news and links",
                testTag = "create_post_opt",
                onClick = {
                    onDismiss()
                    onCreatePost()
                }
            )

            CreateOptionItem(
                icon = Icons.Default.AutoAwesome,
                color = SubirMagenta,
                title = translator("stories"),
                subtitle = "Share a 24-hour photo or message",
                testTag = "create_story_opt",
                onClick = {
                    onDismiss()
                    onCreateStory()
                }
            )

            CreateOptionItem(
                icon = Icons.Default.PlayCircle,
                color = SubirPurple,
                title = translator("reels"),
                subtitle = "Upload full-screen vertical short video",
                testTag = "create_reel_opt",
                onClick = {
                    onDismiss()
                    onCreateReel()
                }
            )

            CreateOptionItem(
                icon = Icons.Default.Image,
                color = SubirOrange,
                title = translator("photos"),
                subtitle = "Share photos with your followers",
                testTag = "create_photo_opt",
                onClick = {
                    onDismiss()
                    onUploadPhoto()
                }
            )

            CreateOptionItem(
                icon = Icons.Default.Videocam,
                color = Color(0xFF4CAF50),
                title = translator("videos"),
                subtitle = "Upload video with description",
                testTag = "create_video_opt",
                onClick = {
                    onDismiss()
                    onUploadVideo()
                }
            )
        }
    }
}

@Composable
private fun CreateOptionItem(
    icon: ImageVector,
    color: Color,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
