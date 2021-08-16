package com.volvadvit.messenger.api.helpers.objects

data class UserVO (
    val id: Long,
    val username: String,
    val phoneNumber: String,
    val status: String,
    val createdAt: String
)

data class UserListVO (
    val users: List<UserVO>
)

data class MessageVO (
    val id: Long,
    val senderId: Long,
    val recipientId: Long,
    val conversationId: Long?,
    val body: String?,
    val createdAt: String
)

data class ConversationVO (
    val conversationId: Long?,
    val secondPartyUsername: String,
    val messages: ArrayList<MessageVO>
)

data class ConversationListVO(
    val conversations: List<ConversationVO>
)