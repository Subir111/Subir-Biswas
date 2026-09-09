package com.example.data.repository

import com.example.data.locale.AppLanguage
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

object SubirRepository {

    // Language & Theme State
    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Current User
    private val defaultUser = User(
        id = "user_me",
        name = "Subir Hasan",
        username = "subir_creator",
        email = "subir@subir.social",
        phone = "+880 1712 345678",
        bio = "Digital storyteller & UI architect 🚀 Building the future of social media at Subir. Welcome to my creative space!",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
        coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1000&q=80",
        isVerified = true,
        followersCount = 14820,
        followingCount = 485,
        friendsCount = 920,
        isProfessional = true,
        role = UserRole.SUPER_ADMIN,
        location = "Dhaka, Bangladesh",
        website = "https://subir.social/@subir_creator",
        birthDate = "1998-08-20"
    )

    private val _currentUser = MutableStateFlow<User?>(defaultUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Search History
    private val _searchHistory = MutableStateFlow(listOf("Dhaka Meetup", "#SubirCommunity", "Tasnim Rahman", "#TechTrends"))
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    // Sample Users for Community
    val sampleUsers = listOf(
        User(
            id = "u_1",
            name = "Ayesha Karim",
            username = "ayesha_k",
            email = "ayesha@subir.social",
            bio = "Visual artist & photographer 🎨 Nature lover | Chasing golden hours in Cox's Bazar",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1000&q=80",
            isVerified = true,
            followersCount = 38500,
            followingCount = 310,
            friendsCount = 1420,
            location = "Chittagong, Bangladesh",
            isFollowing = true,
            isFriend = true
        ),
        User(
            id = "u_2",
            name = "Tanvir Ahmed",
            username = "tanvir_code",
            email = "tanvir@subir.social",
            bio = "Mobile dev, Kotlin enthusiast ☕ Passionate about open-source & clean UI",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=1000&q=80",
            isVerified = false,
            followersCount = 8900,
            followingCount = 420,
            friendsCount = 630,
            location = "Sylhet, Bangladesh",
            isFollowing = false,
            isFriend = false,
            hasFriendRequestPending = true
        ),
        User(
            id = "u_3",
            name = "Nabila Chowdhury",
            username = "nabila_vibe",
            email = "nabila@subir.social",
            bio = "Singer, songwriter & acoustic guitar player 🎸 New music video out now!",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1000&q=80",
            isVerified = true,
            followersCount = 62400,
            followingCount = 190,
            friendsCount = 1840,
            location = "Dhaka, Bangladesh",
            isFollowing = true,
            isFriend = true
        ),
        User(
            id = "u_4",
            name = "Rahim Uddin",
            username = "rahim_wander",
            email = "rahim@subir.social",
            bio = "Travel filmmaker 🎒 Exploring hills of Bandarban & rivers of Barisal",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=1000&q=80",
            isVerified = false,
            followersCount = 12100,
            followingCount = 640,
            friendsCount = 410,
            location = "Bandarban, Bangladesh",
            isFollowing = false,
            isFriend = false
        )
    )

    // Stories State
    private val _stories = MutableStateFlow(
        listOf(
            Story(
                id = "story_me",
                author = defaultUser,
                mediaUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
                text = "Welcome to the official Subir launch! 🚀✨",
                backgroundHex = "#0B1528",
                isViewed = false
            ),
            Story(
                id = "s_1",
                author = sampleUsers[0],
                mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
                text = "Sunset at Patenga beach was magical today 🌅",
                backgroundHex = "#FF5722",
                isViewed = false
            ),
            Story(
                id = "s_2",
                author = sampleUsers[1],
                mediaUrl = null,
                text = "Pushing new features to Subir Android app! Kotlin + Jetpack Compose is 🔥",
                backgroundHex = "#4A148C",
                isViewed = false
            ),
            Story(
                id = "s_3",
                author = sampleUsers[2],
                mediaUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
                text = "Acoustic jamming session with friends 🎶 Drop song requests!",
                backgroundHex = "#00695C",
                isViewed = true
            )
        )
    )
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    // Posts Feed State
    private val _posts = MutableStateFlow(
        listOf(
            Post(
                id = "p_1",
                author = defaultUser,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                content = "Excited to introduce SUBIR — an authentic social media platform crafted for true connection, creative storytelling, and empowered creators! ✨\n\nDrop a comment and share your favorite moments. Let's build a vibrant community together! 🇧🇩🌟 #SubirOfficial #Community #CreatorEconomy #SocialVibes",
                mediaUrls = listOf(
                    "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1000&q=80"
                ),
                hashtags = listOf("SubirOfficial", "Community", "CreatorEconomy", "SocialVibes"),
                location = "Gulshan, Dhaka",
                feeling = "feeling proud 🌟",
                likesCount = 342,
                commentsCount = 28,
                sharesCount = 14,
                userReaction = ReactionType.LOVE,
                isSaved = true
            ),
            Post(
                id = "p_2",
                author = sampleUsers[0],
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                content = "Woke up at 5 AM to catch this breathtaking mist over the tea gardens of Sreemangal. There is nowhere in the world quite like rural Bengal in the early morning silence. 🍃☕\n\nCamera gear: 35mm f/1.4 lens. #Photography #BengalBeauty #Sreemangal #TravelDiaries",
                mediaUrls = listOf(
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1000&q=80",
                    "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=1000&q=80"
                ),
                hashtags = listOf("Photography", "BengalBeauty", "Sreemangal", "TravelDiaries"),
                location = "Sreemangal, Sylhet",
                feeling = "feeling peaceful 🌿",
                likesCount = 890,
                commentsCount = 74,
                sharesCount = 52,
                userReaction = ReactionType.LIKE
            ),
            Post(
                id = "p_3",
                author = sampleUsers[2],
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 5,
                content = "My new unplugged Rabindra Sangeet cover is finally live on Subir Reels! 🎵 Tell me which songs you'd love to hear next in the comments. Thank you all for the love and support! ❤️ #BengaliMusic #AcousticCover #NabilaVibes",
                mediaUrls = listOf(
                    "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1000&q=80"
                ),
                isVideo = true,
                hashtags = listOf("BengaliMusic", "AcousticCover", "NabilaVibes"),
                location = "Dhanmondi Lake, Dhaka",
                feeling = "feeling inspired 🎶",
                likesCount = 1250,
                commentsCount = 112,
                sharesCount = 88,
                userReaction = ReactionType.WOW
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Comments State per Post
    private val _comments = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "p_1" to listOf(
                Comment(
                    id = "c_1",
                    postId = "p_1",
                    author = sampleUsers[0],
                    content = "Congratulations Subir! The design is breathtaking, silky smooth and truly original! 👏🎉",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 10,
                    likesCount = 18,
                    isLiked = true
                ),
                Comment(
                    id = "c_2",
                    postId = "p_1",
                    author = sampleUsers[1],
                    content = "In love with the neon aesthetics and Bengali language support. Proud moment! 🇧🇩🔥",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                    likesCount = 9,
                    isLiked = false
                )
            ),
            "p_2" to listOf(
                Comment(
                    id = "c_3",
                    postId = "p_2",
                    author = sampleUsers[2],
                    content = "Stunning shot Ayesha! The morning mist looks surreal.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    likesCount = 14,
                    isLiked = true
                )
            )
        )
    )
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    // Reels State
    private val _reels = MutableStateFlow(
        listOf(
            Reel(
                id = "r_1",
                creator = sampleUsers[2],
                videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-guitarist-playing-acoustic-guitar-42171-large.mp4",
                thumbnail = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
                caption = "Acoustic sunset chords in Dhaka 🎸✨ #Acoustic #BengaliMusic #Dhaka",
                musicTitle = "Nabila - Sunset Melodies (Original)",
                likesCount = 2840,
                commentsCount = 145,
                sharesCount = 312,
                viewsCount = 42500,
                isLiked = true
            ),
            Reel(
                id = "r_2",
                creator = sampleUsers[0],
                videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-aerial-view-of-a-beautiful-coast-42861-large.mp4",
                thumbnail = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
                caption = "Golden horizon over Saint Martin's Island 🌊 Blue waters of Bengal! #SaintMartin #Travel",
                musicTitle = "Ambient Chill Waves 🌊",
                likesCount = 5910,
                commentsCount = 380,
                sharesCount = 689,
                viewsCount = 98100,
                isLiked = false
            ),
            Reel(
                id = "r_3",
                creator = defaultUser,
                videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-hands-typing-on-a-laptop-keyboard-41312-large.mp4",
                thumbnail = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&q=80",
                caption = "Behind the scenes crafting Subir UI animations in Kotlin Compose! 💻⚡ #CodeLife #SubirApp",
                musicTitle = "Synthwave Neon Beats ⚡",
                likesCount = 4120,
                commentsCount = 210,
                sharesCount = 430,
                viewsCount = 65200,
                isLiked = true
            )
        )
    )
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

    // Messenger & Conversations State
    private val _conversations = MutableStateFlow(
        listOf(
            ChatConversation(
                id = "chat_1",
                recipient = sampleUsers[0],
                lastMessage = "Looking forward to our photography showcase next week! 📸",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 12,
                unreadCount = 2,
                messages = listOf(
                    ChatMessage("m_1", sampleUsers[0].id, "Hey Subir, how is the new build going?", System.currentTimeMillis() - 1000 * 60 * 60),
                    ChatMessage("m_2", "user_me", "Going amazingly! The UI is responsive and fast.", System.currentTimeMillis() - 1000 * 60 * 30),
                    ChatMessage("m_3", sampleUsers[0].id, "Looking forward to our photography showcase next week! 📸", System.currentTimeMillis() - 1000 * 60 * 12)
                )
            ),
            ChatConversation(
                id = "chat_2",
                recipient = sampleUsers[1],
                lastMessage = "Let's review the pull request together tomorrow morning.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("m_4", sampleUsers[1].id, "Let's review the pull request together tomorrow morning.", System.currentTimeMillis() - 1000 * 60 * 120)
                )
            ),
            ChatConversation(
                id = "chat_group_1",
                recipient = sampleUsers[2],
                lastMessage = "Tanvir: I shared the presentation slides in the drive!",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 240,
                unreadCount = 1,
                isGroup = true,
                groupName = "Subir Creator Community 🇧🇩",
                messages = listOf(
                    ChatMessage("m_5", "u_2", "Tanvir: I shared the presentation slides in the drive!", System.currentTimeMillis() - 1000 * 60 * 240)
                )
            )
        )
    )
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    // Notifications State
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "notif_1",
                type = NotificationType.LOVE,
                actor = sampleUsers[0],
                message = "loved your post: 'Excited to introduce SUBIR...'",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 14,
                isRead = false,
                targetId = "p_1"
            ),
            NotificationItem(
                id = "notif_2",
                type = NotificationType.FRIEND_REQUEST,
                actor = sampleUsers[1],
                message = "sent you a friend request.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                isRead = false
            ),
            NotificationItem(
                id = "notif_3",
                type = NotificationType.COMMENT,
                actor = sampleUsers[2],
                message = "commented on your photo: 'Stunning design! 🎉'",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                isRead = true,
                targetId = "p_1"
            ),
            NotificationItem(
                id = "notif_4",
                type = NotificationType.FOLLOW,
                actor = sampleUsers[3],
                message = "started following you.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 360,
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Blocked Users State
    private val _blockedUsers = MutableStateFlow<List<User>>(emptyList())
    val blockedUsers: StateFlow<List<User>> = _blockedUsers.asStateFlow()

    // Admin Reports State
    private val _reports = MutableStateFlow(
        listOf(
            ReportItem(
                id = "rep_1",
                targetType = "post",
                targetId = "p_99",
                targetTitle = "Suspicious cryptocurrency spam link",
                reportedBy = "ayesha_k",
                reason = "Spam / Misleading content",
                status = "New",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 120
            ),
            ReportItem(
                id = "rep_2",
                targetType = "user",
                targetId = "u_fake",
                targetTitle = "Impersonation account: @official_subir_clone",
                reportedBy = "tanvir_code",
                reason = "Fake Account",
                status = "Reviewing",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 300
            )
        )
    )
    val reports: StateFlow<List<ReportItem>> = _reports.asStateFlow()

    // All Users for Admin Panel
    private val _allUsers = MutableStateFlow(
        listOf(defaultUser) + sampleUsers + listOf(
            User(
                id = "u_pending_1",
                name = "Dr. Shamsul Huda",
                username = "dr_shamsul",
                email = "shamsul@dhaka.edu",
                bio = "Professor & Tech Researcher | AI Ethics",
                avatarUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&q=80",
                isVerified = false,
                followersCount = 4200,
                followingCount = 120,
                friendsCount = 380,
                location = "Dhaka"
            )
        )
    )
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    // Professional Analytics
    private val _analytics = MutableStateFlow(ProfessionalAnalytics())
    val analytics: StateFlow<ProfessionalAnalytics> = _analytics.asStateFlow()

    // --- MUTATION METHODS ---

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun loginUser(emailOrUsername: String, pass: String): Boolean {
        // Find existing or mock user login
        val found = _allUsers.value.find { it.email.equals(emailOrUsername, true) || it.username.equals(emailOrUsername, true) }
        _currentUser.value = found ?: defaultUser.copy(email = emailOrUsername)
        return true
    }

    fun registerUser(name: String, username: String, email: String, phone: String): User {
        val newUser = User(
            id = "u_${UUID.randomUUID().toString().take(8)}",
            name = name,
            username = username,
            email = email,
            phone = phone,
            bio = "Hey there! I just joined Subir ✨",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1000&q=80",
            joinedDate = "September 2026"
        )
        _allUsers.value = listOf(newUser) + _allUsers.value
        _currentUser.value = newUser
        return newUser
    }

    fun logout() {
        _currentUser.value = null
    }

    fun updateProfile(name: String, username: String, bio: String, location: String, website: String) {
        val current = _currentUser.value ?: return
        val updated = current.copy(
            name = name,
            username = username,
            bio = bio,
            location = location,
            website = website
        )
        _currentUser.value = updated
        _allUsers.value = _allUsers.value.map { if (it.id == updated.id) updated else it }
    }

    fun toggleProfessionalMode() {
        val current = _currentUser.value ?: return
        val updated = current.copy(isProfessional = !current.isProfessional)
        _currentUser.value = updated
    }

    fun createPost(content: String, mediaUrls: List<String>, feeling: String?, location: String?, privacy: PostPrivacy) {
        val author = _currentUser.value ?: defaultUser
        val newPost = Post(
            id = "p_${UUID.randomUUID().toString().take(8)}",
            author = author,
            timestamp = System.currentTimeMillis(),
            content = content,
            mediaUrls = mediaUrls,
            feeling = feeling,
            location = location,
            privacy = privacy,
            likesCount = 1,
            userReaction = ReactionType.LIKE
        )
        _posts.value = listOf(newPost) + _posts.value
    }

    fun toggleReaction(postId: String, reaction: ReactionType) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                if (post.userReaction == reaction) {
                    // Remove reaction
                    post.copy(
                        userReaction = null,
                        likesCount = (post.likesCount - 1).coerceAtLeast(0)
                    )
                } else {
                    val countDiff = if (post.userReaction == null) 1 else 0
                    post.copy(
                        userReaction = reaction,
                        likesCount = post.likesCount + countDiff
                    )
                }
            } else post
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
        }
    }

    fun deletePost(postId: String) {
        _posts.value = _posts.value.filterNot { it.id == postId }
    }

    fun addComment(postId: String, text: String) {
        val author = _currentUser.value ?: defaultUser
        val newComment = Comment(
            id = "c_${UUID.randomUUID().toString().take(8)}",
            postId = postId,
            author = author,
            content = text,
            timestamp = System.currentTimeMillis()
        )
        val currentComments = _comments.value[postId] ?: emptyList()
        _comments.value = _comments.value + (postId to (currentComments + newComment))

        // Update post comment count
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(commentsCount = post.commentsCount + 1) else post
        }
    }

    fun toggleLikeComment(postId: String, commentId: String) {
        val currentComments = _comments.value[postId] ?: return
        val updated = currentComments.map { comment ->
            if (comment.id == commentId) {
                val isLikedNow = !comment.isLiked
                comment.copy(
                    isLiked = isLikedNow,
                    likesCount = comment.likesCount + (if (isLikedNow) 1 else -1)
                )
            } else comment
        }
        _comments.value = _comments.value + (postId to updated)
    }

    fun toggleLikeReel(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) {
                val likedNow = !reel.isLiked
                reel.copy(
                    isLiked = likedNow,
                    likesCount = reel.likesCount + (if (likedNow) 1 else -1)
                )
            } else reel
        }
    }

    fun toggleSaveReel(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) reel.copy(isSaved = !reel.isSaved) else reel
        }
    }

    fun addStory(text: String?, mediaUrl: String?, bgHex: String) {
        val author = _currentUser.value ?: defaultUser
        val newStory = Story(
            id = "s_${UUID.randomUUID().toString().take(8)}",
            author = author,
            mediaUrl = mediaUrl,
            text = text,
            backgroundHex = bgHex,
            timestamp = System.currentTimeMillis()
        )
        _stories.value = listOf(newStory) + _stories.value
    }

    fun markStoryViewed(storyId: String) {
        _stories.value = _stories.value.map { if (it.id == storyId) it.copy(isViewed = true) else it }
    }

    fun followUser(userId: String) {
        _allUsers.value = _allUsers.value.map { u ->
            if (u.id == userId) {
                val newStatus = !u.isFollowing
                u.copy(
                    isFollowing = newStatus,
                    followersCount = u.followersCount + (if (newStatus) 1 else -1)
                )
            } else u
        }
    }

    fun sendFriendRequest(userId: String) {
        _allUsers.value = _allUsers.value.map { u ->
            if (u.id == userId) u.copy(hasFriendRequestPending = true) else u
        }
    }

    fun acceptFriendRequest(userId: String) {
        _allUsers.value = _allUsers.value.map { u ->
            if (u.id == userId) u.copy(
                hasFriendRequestPending = false,
                isFriend = true,
                friendsCount = u.friendsCount + 1
            ) else u
        }
        // Also add notification
        val user = _allUsers.value.find { it.id == userId } ?: return
        val newNotif = NotificationItem(
            id = "notif_${UUID.randomUUID().toString().take(8)}",
            type = NotificationType.FRIEND_ACCEPTED,
            actor = user,
            message = "accepted your friend request. You are now friends on Subir!",
            timestamp = System.currentTimeMillis()
        )
        _notifications.value = listOf(newNotif) + _notifications.value
    }

    fun rejectFriendRequest(userId: String) {
        _allUsers.value = _allUsers.value.map { u ->
            if (u.id == userId) u.copy(hasFriendRequestPending = false) else u
        }
    }

    fun sendMessage(chatId: String, text: String, mediaUrl: String? = null) {
        val newMsg = ChatMessage(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            senderId = "user_me",
            text = text,
            mediaUrl = mediaUrl,
            timestamp = System.currentTimeMillis(),
            isSeen = false
        )
        _conversations.value = _conversations.value.map { convo ->
            if (convo.id == chatId) {
                convo.copy(
                    lastMessage = text,
                    lastTimestamp = System.currentTimeMillis(),
                    messages = convo.messages + newMsg
                )
            } else convo
        }
    }

    fun markNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun blockUser(user: User) {
        if (!_blockedUsers.value.any { it.id == user.id }) {
            _blockedUsers.value = _blockedUsers.value + user
        }
    }

    fun unblockUser(userId: String) {
        _blockedUsers.value = _blockedUsers.value.filterNot { it.id == userId }
    }

    fun submitReport(targetType: String, targetId: String, targetTitle: String, reason: String) {
        val newRep = ReportItem(
            id = "rep_${UUID.randomUUID().toString().take(8)}",
            targetType = targetType,
            targetId = targetId,
            targetTitle = targetTitle,
            reportedBy = _currentUser.value?.username ?: "anonymous",
            reason = reason,
            status = "New",
            timestamp = System.currentTimeMillis()
        )
        _reports.value = listOf(newRep) + _reports.value
    }

    // Admin Actions
    fun adminVerifyUser(userId: String) {
        _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(isVerified = !it.isVerified) else it }
    }

    fun adminUpdateReportStatus(reportId: String, newStatus: String) {
        _reports.value = _reports.value.map { if (it.id == reportId) it.copy(status = newStatus) else it }
    }

    fun adminBroadcastAnnouncement(title: String, message: String) {
        val adminUser = _currentUser.value ?: defaultUser
        val announcement = NotificationItem(
            id = "ann_${UUID.randomUUID().toString().take(8)}",
            type = NotificationType.MENTION,
            actor = adminUser,
            message = "📢 Subir Official Announcement: $title — $message",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        _notifications.value = listOf(announcement) + _notifications.value
    }

    fun addSearchQuery(query: String) {
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            _searchHistory.value = listOf(query) + _searchHistory.value.take(7)
        }
    }

    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }
}
