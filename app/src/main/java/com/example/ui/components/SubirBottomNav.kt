package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SubirBrandGradient
import com.example.ui.theme.SubirCyan
import com.example.ui.theme.SubirMagenta
import com.example.ui.theme.SubirPurple

enum class BottomNavDestination(
    val route: String,
    val titleKey: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "home", Icons.Filled.Home, Icons.Outlined.Home),
    REELS("reels", "reels", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircleOutline),
    NOTIFICATIONS("notifications", "notifications", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    PROFILE("profile", "profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
}

@Composable
fun SubirBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCreateClick: () -> Unit,
    languageTranslator: (String) -> String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            val homeSelected = currentRoute == BottomNavDestination.HOME.route
            NavItem(
                destination = BottomNavDestination.HOME,
                isSelected = homeSelected,
                label = languageTranslator(BottomNavDestination.HOME.titleKey),
                onClick = { onNavigate(BottomNavDestination.HOME.route) }
            )

            // Reels
            val reelsSelected = currentRoute == BottomNavDestination.REELS.route
            NavItem(
                destination = BottomNavDestination.REELS,
                isSelected = reelsSelected,
                label = languageTranslator(BottomNavDestination.REELS.titleKey),
                onClick = { onNavigate(BottomNavDestination.REELS.route) }
            )

            // Center Create Action FAB Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = SubirCyan, spotColor = SubirMagenta)
                    .clip(CircleShape)
                    .background(SubirBrandGradient)
                    .clickable { onCreateClick() }
                    .testTag("nav_create_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Notifications
            val notifSelected = currentRoute == BottomNavDestination.NOTIFICATIONS.route
            NavItem(
                destination = BottomNavDestination.NOTIFICATIONS,
                isSelected = notifSelected,
                label = languageTranslator(BottomNavDestination.NOTIFICATIONS.titleKey),
                onClick = { onNavigate(BottomNavDestination.NOTIFICATIONS.route) }
            )

            // Profile
            val profileSelected = currentRoute == BottomNavDestination.PROFILE.route
            NavItem(
                destination = BottomNavDestination.PROFILE,
                isSelected = profileSelected,
                label = languageTranslator(BottomNavDestination.PROFILE.titleKey),
                onClick = { onNavigate(BottomNavDestination.PROFILE.route) }
            )
        }
    }
}

@Composable
private fun NavItem(
    destination: BottomNavDestination,
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("nav_item_${destination.route}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
