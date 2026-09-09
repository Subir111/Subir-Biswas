package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.model.Post
import com.example.data.model.ReactionType
import com.example.data.model.Story
import com.example.data.repository.SubirRepository
import com.example.ui.components.ReactionPickerPopup
import com.example.ui.components.SubirHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onMessengerClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    onCreateStoryClick: () -> Unit,
    onStoryClick: (Story) -> Unit,
    onPostClick: (Post) -> Unit,
    onOpenComments: (Post) -> Unit,
    onUserProfileClick: (String) -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val posts by SubirRepository.posts.collectAsState()
    val stories by SubirRepository.stories.collectAsState()
    val currentUser by SubirRepository.currentUser.collectAsState()
    val context = LocalContext.current

    var selectedFeedTab by remember { mutableStateOf(0) } // 0: For You, 1: Following, 2: Friends
    var isRefreshing by remember { mutableStateOf(false) }

    // Share & Report Dialogs
    var postToShare by remember { mutableStateOf<Post?>(null) }
    var postToReport by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        topBar = {
            SubirHeader(
                unreadMessages = 2,
                unreadNotifications = 2,
                onSearchClick = onSearchClick,
                onMessengerClick = onMessengerClick,
                onNotificationClick = onNotificationClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("home_feed_list")
        ) {
            // "What's on your mind?" quick composer bar
            item {
                QuickComposerBar(
                    avatarUrl = currentUser?.avatarUrl ?: "",
                    userName = currentUser?.name ?: "User",
                    onClick = onCreatePostClick,
                    placeholder = SubirStrings.get("whats_on_your_mind", lang)
                )
            }

            // Stories Tray
            item {
                StoriesSection(
                    stories = stories,
                    userAvatar = currentUser?.avatarUrl ?: "",
                    onAddStory = onCreateStoryClick,
                    onStoryClick = onStoryClick,
                    langTitle = SubirStrings.get("your_story", lang)
                )
            }

            // Feed Category Tabs
            item {
                FeedTabsRow(
                    selectedIndex = selectedFeedTab,
                    onSelect = { selectedFeedTab = it },
                    labels = listOf(
                        SubirStrings.get("for_you", lang),
                        SubirStrings.get("following", lang),
                        SubirStrings.get("friends", lang)
                    )
                )
            }

            // Filtered Posts List
            val filteredPosts = when (selectedFeedTab) {
                1 -> posts.filter { it.author.isFollowing || it.author.id == currentUser?.id }
                2 -> posts.filter { it.author.isFriend || it.author.id == currentUser?.id }
                else -> posts
            }

            if (filteredPosts.isEmpty()) {
                item {
                    EmptyFeedState(
                        message = SubirStrings.get("no_posts_yet", lang),
                        onCreatePost = onCreatePostClick
                    )
                }
            } else {
                items(filteredPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onReactionSelected = { reaction ->
                            SubirRepository.toggleReaction(post.id, reaction)
                        },
                        onCommentClick = { onOpenComments(post) },
                        onShareClick = { postToShare = post },
                        onSaveClick = { SubirRepository.toggleSavePost(post.id) },
                        onReportClick = { postToReport = post },
                        onDeleteClick = { SubirRepository.deletePost(post.id) },
                        onAuthorClick = { onUserProfileClick(post.author.id) },
                        isOwnPost = post.author.id == currentUser?.id
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Share Sheet
    if (postToShare != null) {
        val post = postToShare!!
        ModalBottomSheet(
            onDismissRequest = { postToShare = null },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = SubirStrings.get("share", lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                ShareOptionItem(Icons.Default.Send, "Share to Subir Feed") {
                    Toast.makeText(context, "Shared to your Subir feed!", Toast.LENGTH_SHORT).show()
                    postToShare = null
                }
                ShareOptionItem(Icons.Default.AutoAwesome, "Share to Your Story") {
                    Toast.makeText(context, "Shared to your story!", Toast.LENGTH_SHORT).show()
                    postToShare = null
                }
                ShareOptionItem(Icons.Default.Chat, "Send in Messenger") {
                    Toast.makeText(context, "Message sent to friends!", Toast.LENGTH_SHORT).show()
                    postToShare = null
                }
                ShareOptionItem(Icons.Default.ContentCopy, "Copy Link") {
                    Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                    postToShare = null
                }
            }
        }
    }

    // Report Dialog
    if (postToReport != null) {
        val post = postToReport!!
        var selectedReason by remember { mutableStateOf("Spam") }
        AlertDialog(
            onDismissRequest = { postToReport = null },
            title = { Text(SubirStrings.get("report", lang)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(SubirStrings.get("report_reason", lang), fontSize = 14.sp)
                    val reasons = listOf("Spam", "Harassment", "Fake Account", "Hate Speech", "Violence", "Copyright")
                    reasons.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = r }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(selected = selectedReason == r, onClick = { selectedReason = r })
                            Text(text = r, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    SubirRepository.submitReport("post", post.id, post.content.take(30), selectedReason)
                    Toast.makeText(context, SubirStrings.get("report_submitted", lang), Toast.LENGTH_LONG).show()
                    postToReport = null
                }) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { postToReport = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun QuickComposerBar(
    avatarUrl: String,
    userName: String,
    onClick: () -> Unit,
    placeholder: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
            .testTag("quick_composer_bar"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = userName,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Attach image",
                tint = SubirCyan,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun StoriesSection(
    stories: List<Story>,
    userAvatar: String,
    onAddStory: () -> Unit,
    onStoryClick: (Story) -> Unit,
    langTitle: String
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // "Your Story" item with plus icon
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onAddStory() }
                    .testTag("add_story_item")
            ) {
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = userAvatar,
                        contentDescription = "Your Story",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(SubirCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = langTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Other Stories
        items(stories) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onStoryClick(story) }
                    .testTag("story_item_${story.id}")
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.5.dp,
                            brush = if (story.isViewed) Brush.linearGradient(listOf(Color.Gray, Color.DarkGray)) else SubirStoryGradient,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = story.author.avatarUrl,
                        contentDescription = story.author.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.author.name.split(" ").firstOrNull() ?: story.author.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FeedTabsRow(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    labels: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val isSelected = selectedIndex == index
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(index) },
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: Post,
    onReactionSelected: (ReactionType) -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAuthorClick: () -> Unit,
    isOwnPost: Boolean
) {
    var showReactions by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable { onAuthorClick() }
                ) {
                    AsyncImage(
                        model = post.author.avatarUrl,
                        contentDescription = post.author.name,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = post.author.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (post.author.isVerified) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = SubirCyan,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "@${post.author.username}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(text = "•", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "2h ago",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Options Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (post.isSaved) "Remove from Saved" else "Save Post") },
                            leadingIcon = { Icon(if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onSaveClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Report Post") },
                            leadingIcon = { Icon(Icons.Default.ReportProblem, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onReportClick()
                            }
                        )
                        if (isOwnPost) {
                            DropdownMenuItem(
                                text = { Text("Delete Post", color = Color(0xFFF44336)) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF44336)) },
                                onClick = {
                                    showMenu = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            // Location or Feeling Tags
            if (post.location != null || post.feeling != null) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (post.location != null) {
                        Text(
                            text = "📍 ${post.location}",
                            fontSize = 12.sp,
                            color = SubirCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (post.feeling != null) {
                        Text(
                            text = "✨ ${post.feeling}",
                            fontSize = 12.sp,
                            color = SubirMagenta,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Post Content Text
            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    fontSize = 14.5.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            // Media Gallery
            if (post.mediaUrls.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = post.mediaUrls.first(),
                        contentDescription = "Post media",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (post.isVideo) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }
            }

            // Engagement Counts Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "❤️", fontSize = 13.sp)
                    Text(text = "👍", fontSize = 13.sp)
                    Text(
                        text = "${post.likesCount}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "${post.commentsCount} comments",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${post.sharesCount} shares",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                thickness = 0.8.dp
            )

            // Reaction Picker Popup Overlay
            Box {
                ReactionPickerPopup(
                    visible = showReactions,
                    onReactionSelected = { reaction ->
                        onReactionSelected(reaction)
                        showReactions = false
                    },
                    onDismiss = { showReactions = false },
                    modifier = Modifier.offset(y = (-48).dp)
                )

                // Action Buttons Bar (Like, Comment, Share, Save)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like button (Tap toggles like, long-click triggers full reactions)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    val r = if (post.userReaction != null) post.userReaction!! else ReactionType.LIKE
                                    onReactionSelected(r)
                                },
                                onLongClick = { showReactions = true }
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (post.userReaction != null) {
                            Text(text = post.userReaction.emoji, fontSize = 16.sp)
                            Text(
                                text = post.userReaction.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SubirCyan
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = "Like",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Like",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Comment Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onCommentClick() }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Comment",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Share Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onShareClick() }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Share",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Save Bookmark Button
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) SubirYellow else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EmptyFeedState(
    message: String,
    onCreatePost: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DynamicFeed,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCreatePost) {
            Text("Create Post")
        }
    }
}
