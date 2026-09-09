package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.locale.SubirStrings
import com.example.data.repository.SubirRepository
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirOrange
import com.example.ui.theme.SubirPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onBackClick: () -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val allUsers by SubirRepository.allUsers.collectAsState()
    val reports by SubirRepository.reports.collectAsState()
    val posts by SubirRepository.posts.collectAsState()
    val context = LocalContext.current

    var selectedAdminTab by remember { mutableStateOf(0) } // 0: Overview, 1: Users, 2: Reports, 3: Announcements

    var announcementTitle by remember { mutableStateOf("") }
    var announcementMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(SubirStrings.get("admin_panel", lang), fontWeight = FontWeight.Bold) },
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
                .testTag("admin_dashboard_screen")
        ) {
            // Admin Tabs
            TabRow(selectedTabIndex = selectedAdminTab) {
                Tab(selected = selectedAdminTab == 0, onClick = { selectedAdminTab = 0 }, text = { Text("Overview") })
                Tab(selected = selectedAdminTab == 1, onClick = { selectedAdminTab = 1 }, text = { Text("Users (${allUsers.size})") })
                Tab(selected = selectedAdminTab == 2, onClick = { selectedAdminTab = 2 }, text = { Text("Reports (${reports.size})") })
                Tab(selected = selectedAdminTab == 3, onClick = { selectedAdminTab = 3 }, text = { Text("Broadcast") })
            }

            when (selectedAdminTab) {
                0 -> {
                    // Overview Stats
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AdminStatBox(title = "Total Users", value = "${allUsers.size * 1240}", icon = Icons.Default.Group, color = SubirCyan, modifier = Modifier.weight(1f))
                            AdminStatBox(title = "Total Posts", value = "${posts.size * 850}", icon = Icons.Default.DynamicFeed, color = SubirPurple, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AdminStatBox(title = "Pending Reports", value = "${reports.size}", icon = Icons.Default.Report, color = SubirMagenta, modifier = Modifier.weight(1f))
                            AdminStatBox(title = "System Health", value = "99.98%", icon = Icons.Default.CheckCircle, color = Color(0xFF4CAF50), modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Recent Moderation Queue", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        reports.take(3).forEach { rep ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = rep.targetTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        Text(text = "Reason: ${rep.reason} • Status: ${rep.status}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    AssistChip(
                                        onClick = { SubirRepository.adminUpdateReportStatus(rep.id, "Resolved") },
                                        label = { Text("Resolve") }
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Users Management
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allUsers) { user ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = user.name,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            if (user.isVerified) {
                                                Icon(Icons.Default.Verified, contentDescription = null, tint = SubirCyan, modifier = Modifier.size(15.dp))
                                            }
                                        }
                                        Text(text = "@${user.username} • ${user.role.name}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    // Toggle Verification
                                    IconButton(
                                        onClick = {
                                            SubirRepository.adminVerifyUser(user.id)
                                            Toast.makeText(context, "Verification badge updated for ${user.name}", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (user.isVerified) Icons.Default.Verified else Icons.Default.CheckCircleOutline,
                                            contentDescription = "Verify",
                                            tint = if (user.isVerified) SubirCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Reports Moderation Queue
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(reports) { rep ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Target: ${rep.targetType.uppercase()}", fontWeight = FontWeight.Bold, color = SubirMagenta, fontSize = 12.sp)
                                        Text(text = "Status: ${rep.status}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(text = rep.targetTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(text = "Reported by @${rep.reportedBy} for: ${rep.reason}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                SubirRepository.adminUpdateReportStatus(rep.id, "Resolved")
                                                Toast.makeText(context, "Report marked Resolved", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Resolve", fontSize = 12.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                SubirRepository.adminUpdateReportStatus(rep.id, "Rejected")
                                                Toast.makeText(context, "Report dismissed", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Dismiss", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // System Announcement Broadcast
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Broadcast Platform Announcement",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Send an official notification to every Subir user in English and বাংলা.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = announcementTitle,
                            onValueChange = { announcementTitle = it },
                            label = { Text("Announcement Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = announcementMessage,
                            onValueChange = { announcementMessage = it },
                            label = { Text("Message Body") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )

                        Button(
                            onClick = {
                                if (announcementTitle.isNotBlank() && announcementMessage.isNotBlank()) {
                                    SubirRepository.adminBroadcastAnnouncement(announcementTitle, announcementMessage)
                                    Toast.makeText(context, "Announcement broadcasted to all users!", Toast.LENGTH_LONG).show()
                                    announcementTitle = ""
                                    announcementMessage = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Broadcast Now 📢")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatBox(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
