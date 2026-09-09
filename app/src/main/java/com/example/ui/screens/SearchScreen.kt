package com.example.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.repository.SubirRepository
import com.example.ui.theme.SubirCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onUserProfileClick: (String) -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val allUsers by SubirRepository.allUsers.collectAsState()
    val posts by SubirRepository.posts.collectAsState()
    val reels by SubirRepository.reels.collectAsState()
    val searchHistory by SubirRepository.searchHistory.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(0) } // 0: All, 1: People, 2: Posts, 3: Reels, 4: Tags

    val categories = listOf("All", "People", "Posts", "Reels", "Tags")

    val filteredUsers = allUsers.filter {
        searchQuery.isNotBlank() && (it.name.contains(searchQuery, true) || it.username.contains(searchQuery, true) || it.bio.contains(searchQuery, true))
    }

    val filteredPosts = posts.filter {
        searchQuery.isNotBlank() && (it.content.contains(searchQuery, true) || it.hashtags.any { tag -> tag.contains(searchQuery, true) })
    }

    val filteredReels = reels.filter {
        searchQuery.isNotBlank() && (it.caption.contains(searchQuery, true) || it.musicTitle.contains(searchQuery, true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(SubirStrings.get("search_placeholder", lang), fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_query_input"),
                        shape = RoundedCornerShape(24.dp)
                    )
                },
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
                .testTag("search_screen")
        ) {
            // Filter Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategory,
                edgePadding = 16.dp,
                divider = {}
            ) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedCategory == index,
                        onClick = { selectedCategory = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedCategory == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // If Search query is empty, show Recent Searches and Trending Topics
                if (searchQuery.isBlank()) {
                    if (searchHistory.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = SubirStrings.get("recent_searches", lang),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Clear All",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable { SubirRepository.clearSearchHistory() }
                                )
                            }
                        }

                        items(searchHistory) { historyItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { searchQuery = historyItem }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = historyItem, fontSize = 14.sp)
                            }
                        }
                    }

                    // Trending Topics in Bangladesh
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🔥 " + SubirStrings.get("trending", lang) + " in Bangladesh",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val trending = listOf(
                        "#SubirLaunch" to "42.8K posts",
                        "#DhakaMeetup" to "18.5K posts",
                        "#BangladeshTech" to "12.3K posts",
                        "#CricketFever" to "9.4K posts",
                        "#SreemangalDiaries" to "6.1K posts"
                    )

                    items(trending) { (tag, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { searchQuery = tag }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = tag, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(text = count, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SubirCyan, modifier = Modifier.size(18.dp))
                        }
                    }
                } else {
                    // Search Results
                    if (selectedCategory == 0 || selectedCategory == 1) {
                        if (filteredUsers.isNotEmpty()) {
                            item {
                                Text(text = "People", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            items(filteredUsers) { user ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            SubirRepository.addSearchQuery(searchQuery)
                                            onUserProfileClick(user.id)
                                        }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = user.name,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            if (user.isVerified) {
                                                Icon(Icons.Default.Verified, contentDescription = null, tint = SubirCyan, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Text(text = "@${user.username}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Button(
                                        onClick = { SubirRepository.followUser(user.id) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text(if (user.isFollowing) "Following" else "Follow", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (selectedCategory == 0 || selectedCategory == 2) {
                        if (filteredPosts.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Posts", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            items(filteredPosts) { post ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = post.author.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = post.content, fontSize = 13.sp, maxLines = 3, modifier = Modifier.padding(vertical = 4.dp))
                                        Text(text = "❤️ ${post.likesCount} • 💬 ${post.commentsCount}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    if (filteredUsers.isEmpty() && filteredPosts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                Text("No results found for \"$searchQuery\"", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
