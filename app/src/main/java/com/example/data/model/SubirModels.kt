package com.example.data.model

enum class UserRole {
    USER,
    MODERATOR,
    ADMIN,
    SUPER_ADMIN
}

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val isVerified: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val friendsCount: Int = 0,
    val isProfessional: Boolean = false,
    val role: UserRole = UserRole.USER,
    val location: String = "Dhaka, Bangladesh",
    val website: String = "https://subir.social",
    val birthDate: String = "1998-05-15",
    val isPrivate: Boolean = false,
    val isFollowing: Boolean = false,
    val isFriend: Boolean = false,
    val hasFriendRequestPending: Boolean = false,
    val joinedDate: String = "January 2026"
)

enum class ReactionType(val emoji: String, val label: String) {
    LIKE("👍", "Like"),
    LOVE("❤️", "Love"),
    HAHA("😆", "Haha"),
    WOW("😮", "Wow"),
    SAD("😢", "Sad"),
    ANGRY("😡", "Angry")
}

enum class PostPrivacy(val label: String) {
    PUBLIC("Public"),
    FRIENDS("Friends"),
    ONLY_ME("Only Me")
}

data class Post(
    val id: String,
    val author: User,
    val timestamp: Long,
    val content: String,
    val mediaUrls: List<String> = emptyList(),
    val isVideo: Boolean = false,
    val hashtags: List<String> = emptyList(),
    val location: String? = null,
    val feeling: String? = null,
    val privacy: PostPrivacy = PostPrivacy.PUBLIC,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val userReaction: ReactionType? = null,
    val isSaved: Boolean = false
)

data class Comment(
    val id: String,
    val postId: String,
    val author: User,
    val content: String,
    val timestamp: Long,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<Comment> = emptyList()
)

data class Story(
    val id: String,
    val author: User,
    val mediaUrl: String? = null,
    val isVideo: Boolean = false,
    val text: String? = null,
    val backgroundHex: String = "#1E1E2E",
    val timestamp: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (24 * 3600 * 1000L),
    val viewsCount: Int = 124,
    val isViewed: Boolean = false
)

data class Reel(
    val id: String,
    val creator: User,
    val videoUrl: String,
    val thumbnail: String,
    val caption: String,
    val musicTitle: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val viewsCount: Long,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mediaUrl: String? = null,
    val isSeen: Boolean = true,
    val reaction: String? = null
)

data class ChatConversation(
    val id: String,
    val recipient: User,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val groupName: String? = null,
    val messages: List<ChatMessage> = emptyList()
)

enum class NotificationType {
    LIKE,
    LOVE,
    REACTION,
    COMMENT,
    REPLY,
    SHARE,
    FOLLOW,
    FRIEND_REQUEST,
    FRIEND_ACCEPTED,
    MENTION,
    MESSAGE,
    STORY_REACT,
    REEL_INTERACTION
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val actor: User,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val targetId: String? = null
)

data class ReportItem(
    val id: String,
    val targetType: String, // "post", "user", "comment", "reel", "group"
    val targetId: String,
    val targetTitle: String,
    val reportedBy: String,
    val reason: String,
    val status: String = "New", // "New", "Reviewing", "Resolved", "Rejected"
    val timestamp: Long
)

data class ProfessionalAnalytics(
    val followersGrowth: String = "+18.4%",
    val totalFollowers: String = "24,850",
    val accountsReached: String = "142.6K",
    val engagementRate: String = "9.4%",
    val profileVisits: String = "18,240",
    val postImpressions: String = "320.1K",
    val reelViews: String = "890.5K",
    val estimatedEarnings: String = "$1,450.80",
    val isMonetizationActive: Boolean = true
)
