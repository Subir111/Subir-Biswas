package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.model.User
import com.example.data.repository.SubirRepository
import com.example.ui.theme.SubirBrandGradient
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String? = null,
    onSettingsClick: () -> Unit,
    onProfessionalDashboardClick: () -> Unit,
    onOpenComments: (String) -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val currentUser by SubirRepository.currentUser.collectAsState()
    val allUsers by SubirRepository.allUsers.collectAsState()
    val posts by SubirRepository.posts.collectAsState()
    val reels by SubirRepository.reels.collectAsState()
    val context = LocalContext.current

    val displayedUser = if (userId != null) {
        allUsers.find { it.id == userId } ?: currentUser
    } else {
        currentUser
    }

    if (displayedUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("User not found")
        }
        return
    }

    val isMyProfile = displayedUser.id == currentUser?.id
    var selectedTab by remember { mutableStateOf(0) } // 0: Posts, 1: Photos, 2: Reels, 3: Saved
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val userPosts = posts.filter { it.author.id == displayedUser.id }
    val savedPosts = posts.filter { it.isSaved }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "@${displayedUser.username}", fontWeight = FontWeight.Bold)
                        if (displayedUser.isVerified) {
                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SubirCyan, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                actions = {
                    if (isMyProfile) {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    } else {
                        IconButton(onClick = { Toast.makeText(context, "Profile shared!", Toast.LENGTH_SHORT).show() }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("profile_screen")
        ) {
            // Cover Photo & Profile Avatar
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Cover Photo
                    AsyncImage(
                        model = displayedUser.coverUrl.ifEmpty { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1000&q=80" },
                        contentDescription = "Cover photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Profile Avatar with glowing border
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 90.dp)
                            .size(90.dp)
                            .clip(CircleShape)
                            .border(3.5.dp, SubirBrandGradient, CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(3.dp)
                    ) {
                        AsyncImage(
                            model = displayedUser.avatarUrl,
                            contentDescription = displayedUser.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // User Info & Bio
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = displayedUser.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (displayedUser.isVerified) {
                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SubirCyan, modifier = Modifier.size(18.dp))
                        }
                    }

                    Text(
                        text = "@${displayedUser.username}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (displayedUser.bio.isNotBlank()) {
                        Text(
                            text = displayedUser.bio,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Location, Website, Joined
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (displayedUser.location.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = SubirCyan, modifier = Modifier.size(14.dp))
                                Text(text = displayedUser.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (displayedUser.website.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Link, contentDescription = null, tint = SubirMagenta, modifier = Modifier.size(14.dp))
                                Text(text = displayedUser.website, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Text(text = "Joined ${displayedUser.joinedDate}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Followers / Following / Friends counters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem(label = SubirStrings.get("posts", lang), count = "${userPosts.size}")
                        ProfileStatItem(label = SubirStrings.get("followers", lang), count = "${displayedUser.followersCount}")
                        ProfileStatItem(label = SubirStrings.get("following", lang), count = "${displayedUser.followingCount}")
                        ProfileStatItem(label = SubirStrings.get("friends", lang), count = "${displayedUser.friendsCount}")
                    }

                    // Action Buttons Row
                    if (isMyProfile) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showEditProfileDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(SubirStrings.get("edit_profile", lang), fontSize = 13.sp)
                            }

                            if (displayedUser.isProfessional) {
                                OutlinedButton(
                                    onClick = onProfessionalDashboardClick,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.BarChart, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Dashboard", fontSize = 13.sp)
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { SubirRepository.followUser(displayedUser.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (displayedUser.isFollowing) "Following" else "Follow")
                            }
                            OutlinedButton(
                                onClick = {
                                    if (displayedUser.isFriend) {
                                        Toast.makeText(context, "Already friends!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        SubirRepository.sendFriendRequest(displayedUser.id)
                                        Toast.makeText(context, "Friend request sent!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (displayedUser.isFriend) "Friends" else if (displayedUser.hasFriendRequestPending) "Requested" else "Add Friend")
                            }
                        }
                    }
                }
            }

            // Profile Tabs (Posts, Photos, Reels, Saved)
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.GridOn, contentDescription = "Posts") },
                        text = { Text("Posts") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Image, contentDescription = "Photos") },
                        text = { Text("Photos") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.PlayCircleOutline, contentDescription = "Reels") },
                        text = { Text("Reels") }
                    )
                    if (isMyProfile) {
                        Tab(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = "Saved") },
                            text = { Text("Saved") }
                        )
                    }
                }
            }

            // Content according to selected tab
            when (selectedTab) {
                0 -> {
                    // Posts
                    if (userPosts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                Text("No posts yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(userPosts) { post ->
                            PostCard(
                                post = post,
                                onReactionSelected = { reaction -> SubirRepository.toggleReaction(post.id, reaction) },
                                onCommentClick = { onOpenComments(post.id) },
                                onShareClick = { Toast.makeText(context, "Shared!", Toast.LENGTH_SHORT).show() },
                                onSaveClick = { SubirRepository.toggleSavePost(post.id) },
                                onReportClick = { SubirRepository.submitReport("post", post.id, post.content.take(30), "Inappropriate") },
                                onDeleteClick = { SubirRepository.deletePost(post.id) },
                                onAuthorClick = {},
                                isOwnPost = isMyProfile
                            )
                        }
                    }
                }
                1 -> {
                    // Photos
                    val photos = userPosts.flatMap { it.mediaUrls }
                    if (photos.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                Text("No photos uploaded yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(photos.chunked(3)) { rowPhotos ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                rowPhotos.forEach { photoUrl ->
                                    AsyncImage(
                                        model = photoUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(6.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Reels
                    val userReels = reels.filter { it.creator.id == displayedUser.id }
                    if (userReels.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                Text("No reels posted yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(userReels) { reel ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = reel.thumbnail,
                                    contentDescription = reel.caption,
                                    modifier = Modifier
                                        .size(70.dp, 100.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Column {
                                    Text(text = reel.caption, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(text = "▶ ${reel.viewsCount} views • ❤️ ${reel.likesCount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Saved posts
                    if (savedPosts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                Text("No saved posts yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(savedPosts) { post ->
                            PostCard(
                                post = post,
                                onReactionSelected = { reaction -> SubirRepository.toggleReaction(post.id, reaction) },
                                onCommentClick = { onOpenComments(post.id) },
                                onShareClick = { Toast.makeText(context, "Shared!", Toast.LENGTH_SHORT).show() },
                                onSaveClick = { SubirRepository.toggleSavePost(post.id) },
                                onReportClick = {},
                                onDeleteClick = {},
                                onAuthorClick = {},
                                isOwnPost = false
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog && isMyProfile) {
        var editName by remember { mutableStateOf(displayedUser.name) }
        var editUsername by remember { mutableStateOf(displayedUser.username) }
        var editBio by remember { mutableStateOf(displayedUser.bio) }
        var editLocation by remember { mutableStateOf(displayedUser.location) }
        var editWebsite by remember { mutableStateOf(displayedUser.website) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text(SubirStrings.get("edit_profile", lang)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("Location") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editWebsite,
                        onValueChange = { editWebsite = it },
                        label = { Text("Website") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    SubirRepository.updateProfile(editName, editUsername, editBio, editLocation, editWebsite)
                    showEditProfileDialog = false
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatItem(label: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
