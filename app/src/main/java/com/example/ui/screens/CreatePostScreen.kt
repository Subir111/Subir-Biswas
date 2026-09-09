package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.model.PostPrivacy
import com.example.data.repository.SubirRepository
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirOrange
import com.example.ui.theme.SubirPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onDismiss: () -> Unit,
    onPostCreated: () -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val currentUser by SubirRepository.currentUser.collectAsState()
    val context = LocalContext.current

    var contentText by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf(PostPrivacy.PUBLIC) }
    var selectedMediaUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf<String?>(null) }

    var showFeelingDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showPhotoPicker by remember { mutableStateOf(false) }

    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
        "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
        "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
        "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&q=80",
        "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=800&q=80"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(SubirStrings.get("create", lang) + " " + SubirStrings.get("post", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (contentText.isBlank() && selectedMediaUrls.isEmpty()) {
                                Toast.makeText(context, "Please enter some text or add a photo", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            SubirRepository.createPost(
                                content = contentText,
                                mediaUrls = selectedMediaUrls,
                                feeling = selectedFeeling,
                                location = selectedLocation,
                                privacy = privacy
                            )
                            Toast.makeText(context, "Post published successfully!", Toast.LENGTH_SHORT).show()
                            onPostCreated()
                        },
                        enabled = contentText.isNotBlank() || selectedMediaUrls.isNotEmpty(),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("publish_post_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(SubirStrings.get("post", lang), fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        bottomBar = {
            // Attachment Tools Bar
            Surface(
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showPhotoPicker = true }) {
                        Icon(Icons.Default.Image, contentDescription = "Add Photo", tint = SubirCyan)
                    }
                    IconButton(onClick = {
                        selectedMediaUrls = listOf("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80")
                        Toast.makeText(context, "Video selected!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Add Video", tint = SubirPurple)
                    }
                    IconButton(onClick = { showFeelingDialog = true }) {
                        Icon(Icons.Default.EmojiEmotions, contentDescription = "Feeling", tint = SubirOrange)
                    }
                    IconButton(onClick = { showLocationDialog = true }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = SubirMagenta)
                    }
                    IconButton(onClick = {
                        contentText += " #Subir #Community"
                    }) {
                        Icon(Icons.Default.Tag, contentDescription = "Hashtag", tint = Color(0xFF4CAF50))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Author info + Privacy selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = currentUser?.avatarUrl,
                    contentDescription = currentUser?.name,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Text(
                        text = currentUser?.name ?: "User",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    // Privacy Chip Toggle
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                privacy = when (privacy) {
                                    PostPrivacy.PUBLIC -> PostPrivacy.FRIENDS
                                    PostPrivacy.FRIENDS -> PostPrivacy.ONLY_ME
                                    PostPrivacy.ONLY_ME -> PostPrivacy.PUBLIC
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when (privacy) {
                                PostPrivacy.PUBLIC -> Icons.Default.Public
                                PostPrivacy.FRIENDS -> Icons.Default.Group
                                PostPrivacy.ONLY_ME -> Icons.Default.Lock
                            },
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(text = privacy.label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Feeling / Location badges
            if (selectedFeeling != null || selectedLocation != null) {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (selectedFeeling != null) {
                        AssistChip(
                            onClick = { selectedFeeling = null },
                            label = { Text("Feeling $selectedFeeling") },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp)) }
                        )
                    }
                    if (selectedLocation != null) {
                        AssistChip(
                            onClick = { selectedLocation = null },
                            label = { Text("📍 $selectedLocation") },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Input
            OutlinedTextField(
                value = contentText,
                onValueChange = { contentText = it },
                placeholder = { Text(SubirStrings.get("whats_on_your_mind", lang)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
                    .testTag("create_post_text_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            // Selected Media Previews
            if (selectedMediaUrls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(selectedMediaUrls) { url ->
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { selectedMediaUrls = selectedMediaUrls.filterNot { it == url } },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(28.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Photo Gallery Picker Dialog
    if (showPhotoPicker) {
        AlertDialog(
            onDismissRequest = { showPhotoPicker = false },
            title = { Text("Select Photo from Gallery") },
            text = {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(samplePhotos) { photo ->
                        AsyncImage(
                            model = photo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    selectedMediaUrls = selectedMediaUrls + photo
                                    showPhotoPicker = false
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPhotoPicker = false }) {
                    Text("Done")
                }
            }
        )
    }

    // Feelings Dialog
    if (showFeelingDialog) {
        val feelings = listOf("proud 🌟", "happy 😊", "excited 🎉", "blessed ✨", "motivated 💪", "creative 🎨", "loved ❤️")
        AlertDialog(
            onDismissRequest = { showFeelingDialog = false },
            title = { Text("How are you feeling?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    feelings.forEach { f ->
                        Text(
                            text = f,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFeeling = f
                                    showFeelingDialog = false
                                }
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFeelingDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Location Dialog
    if (showLocationDialog) {
        val locations = listOf("Gulshan, Dhaka", "Dhanmondi Lake, Dhaka", "Cox's Bazar Sea Beach", "Sreemangal Tea Garden", "Saint Martin's Island", "Sylhet City")
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Add Location") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    locations.forEach { loc ->
                        Text(
                            text = "📍 $loc",
                            fontSize = 15.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLocation = loc
                                    showLocationDialog = false
                                }
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
