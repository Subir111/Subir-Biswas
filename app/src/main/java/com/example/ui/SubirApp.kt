package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.locale.SubirStrings
import com.example.data.model.Post
import com.example.data.model.Reel
import com.example.data.model.Story
import com.example.data.repository.SubirRepository
import com.example.ui.components.CreateActionSheet
import com.example.ui.components.SubirBottomNav
import com.example.ui.screens.*
import com.example.ui.theme.SubirTheme

enum class MainAppRoute {
    SPLASH,
    ONBOARDING,
    LOGIN,
    REGISTER,
    MAIN_CONTAINER
}

@Composable
fun SubirApp() {
    SubirTheme {
        val lang by SubirRepository.language.collectAsState()
        val currentUser by SubirRepository.currentUser.collectAsState()
        val context = LocalContext.current

        val navController = rememberNavController()

        // Root Navigation
        NavHost(
            navController = navController,
            startDestination = MainAppRoute.SPLASH.name
        ) {
            composable(MainAppRoute.SPLASH.name) {
                SplashScreen(
                    onSplashFinished = {
                        if (currentUser != null) {
                            navController.navigate(MainAppRoute.MAIN_CONTAINER.name) {
                                popUpTo(MainAppRoute.SPLASH.name) { inclusive = true }
                            }
                        } else {
                            navController.navigate(MainAppRoute.ONBOARDING.name) {
                                popUpTo(MainAppRoute.SPLASH.name) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(MainAppRoute.ONBOARDING.name) {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(MainAppRoute.LOGIN.name) {
                            popUpTo(MainAppRoute.ONBOARDING.name) { inclusive = true }
                        }
                    }
                )
            }

            composable(MainAppRoute.LOGIN.name) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(MainAppRoute.MAIN_CONTAINER.name) {
                            popUpTo(MainAppRoute.LOGIN.name) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(MainAppRoute.REGISTER.name)
                    },
                    onNavigateToForgotPassword = {
                        Toast.makeText(context, "Password reset OTP sent to your phone/email!", Toast.LENGTH_LONG).show()
                    }
                )
            }

            composable(MainAppRoute.REGISTER.name) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(MainAppRoute.MAIN_CONTAINER.name) {
                            popUpTo(MainAppRoute.REGISTER.name) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(MainAppRoute.MAIN_CONTAINER.name) {
                MainContainerScreen(
                    onLogout = {
                        navController.navigate(MainAppRoute.LOGIN.name) {
                            popUpTo(MainAppRoute.MAIN_CONTAINER.name) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainContainerScreen(
    onLogout: () -> Unit
) {
    val lang by SubirRepository.language.collectAsState()
    val context = LocalContext.current

    var currentTab by remember { mutableStateOf("home") }
    var showCreateSheet by remember { mutableStateOf(false) }
    var showCreatePostScreen by remember { mutableStateOf(false) }

    // Overlays
    var activeStoryToView by remember { mutableStateOf<Story?>(null) }
    var activePostForComments by remember { mutableStateOf<String?>(null) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showMessengerScreen by remember { mutableStateOf(false) }
    var showSettingsScreen by remember { mutableStateOf(false) }
    var showProDashboardScreen by remember { mutableStateOf(false) }
    var showAdminDashboardScreen by remember { mutableStateOf(false) }
    var viewingUserId by remember { mutableStateOf<String?>(null) }

    val translator: (String) -> String = { key -> SubirStrings.get(key, lang) }

    Scaffold(
        bottomBar = {
            // Hide bottom nav if full-screen overlay is active
            if (activeStoryToView == null && !showSearchScreen && !showMessengerScreen &&
                !showSettingsScreen && !showProDashboardScreen && !showAdminDashboardScreen && viewingUserId == null
            ) {
                SubirBottomNav(
                    currentRoute = currentTab,
                    onNavigate = { currentTab = it },
                    onCreateClick = { showCreateSheet = true },
                    languageTranslator = translator
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Base Tab Screen
            when (currentTab) {
                "home" -> HomeScreen(
                    onSearchClick = { showSearchScreen = true },
                    onMessengerClick = { showMessengerScreen = true },
                    onNotificationClick = { currentTab = "notifications" },
                    onCreatePostClick = { showCreatePostScreen = true },
                    onCreateStoryClick = {
                        SubirRepository.addStory("Hello Subir community! ✨", null, "#0B1528")
                        Toast.makeText(context, "Story published!", Toast.LENGTH_SHORT).show()
                    },
                    onStoryClick = { story -> activeStoryToView = story },
                    onPostClick = { post -> activePostForComments = post.id },
                    onOpenComments = { post -> activePostForComments = post.id },
                    onUserProfileClick = { uid -> viewingUserId = uid }
                )
                "reels" -> ReelsScreen(
                    onCommentClick = { reel -> Toast.makeText(context, "Opening comments for reel...", Toast.LENGTH_SHORT).show() },
                    onUserProfileClick = { uid -> viewingUserId = uid }
                )
                "notifications" -> NotificationsScreen(
                    onUserProfileClick = { uid -> viewingUserId = uid }
                )
                "profile" -> ProfileScreen(
                    onSettingsClick = { showSettingsScreen = true },
                    onProfessionalDashboardClick = { showProDashboardScreen = true },
                    onOpenComments = { postId -> activePostForComments = postId }
                )
            }

            // Create Post Full Modal
            if (showCreatePostScreen) {
                CreatePostScreen(
                    onDismiss = { showCreatePostScreen = false },
                    onPostCreated = { showCreatePostScreen = false }
                )
            }

            // Viewing Other User Profile
            if (viewingUserId != null) {
                ProfileScreen(
                    userId = viewingUserId,
                    onSettingsClick = {},
                    onProfessionalDashboardClick = {},
                    onOpenComments = { postId -> activePostForComments = postId }
                )
            }

            // Search Overlay
            if (showSearchScreen) {
                SearchScreen(
                    onBackClick = { showSearchScreen = false },
                    onUserProfileClick = { uid ->
                        showSearchScreen = false
                        viewingUserId = uid
                    }
                )
            }

            // Messenger Overlay
            if (showMessengerScreen) {
                MessengerScreen(
                    onBackClick = { showMessengerScreen = false }
                )
            }

            // Settings Overlay
            if (showSettingsScreen) {
                SettingsScreen(
                    onBackClick = { showSettingsScreen = false },
                    onNavigateToAdmin = {
                        showSettingsScreen = false
                        showAdminDashboardScreen = true
                    },
                    onLogout = {
                        showSettingsScreen = false
                        onLogout()
                    }
                )
            }

            // Professional Dashboard Overlay
            if (showProDashboardScreen) {
                ProfessionalDashboardScreen(
                    onBackClick = { showProDashboardScreen = false }
                )
            }

            // Admin Dashboard Overlay
            if (showAdminDashboardScreen) {
                AdminDashboardScreen(
                    onBackClick = { showAdminDashboardScreen = false }
                )
            }

            // Story Viewer Overlay
            if (activeStoryToView != null) {
                StoryViewerScreen(
                    initialStory = activeStoryToView!!,
                    onDismiss = { activeStoryToView = null }
                )
            }

            // Comments Sheet
            if (activePostForComments != null) {
                CommentsSheet(
                    postId = activePostForComments!!,
                    isOpen = true,
                    onDismiss = { activePostForComments = null }
                )
            }

            // Create Action Sheet
            CreateActionSheet(
                isOpen = showCreateSheet,
                onDismiss = { showCreateSheet = false },
                onCreatePost = { showCreatePostScreen = true },
                onCreateStory = {
                    SubirRepository.addStory("Shared from quick action ✨", null, "#7C4DFF")
                    Toast.makeText(context, "Story published!", Toast.LENGTH_SHORT).show()
                },
                onCreateReel = {
                    Toast.makeText(context, "Reel recorder opened! Picked sample clip.", Toast.LENGTH_SHORT).show()
                },
                onUploadPhoto = {
                    showCreatePostScreen = true
                },
                onUploadVideo = {
                    showCreatePostScreen = true
                },
                translator = translator
            )
        }
    }
}
