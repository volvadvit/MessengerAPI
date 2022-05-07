package com.volvadvit.messenger.api.v1.models

import com.volvadvit.messenger.constants.Role
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.sql.Timestamp

data class MessageRequest(val recipientId: Long, val message: String)

@ApiModel("Common json format for all responses")
data class ResponseMapper(
    @ApiModelProperty("Status of response, like http status")
    val status: Int,
    @ApiModelProperty("Additional message with error/success message")
    val message: String,
    @ApiModelProperty("Response model of required entity")
    val body: Any?,
    @ApiModelProperty("Timestamp of response1")
    val timestamp: Timestamp
)

data class UserVO (
    val id: Long,
    val username: String,
    val photoUrl: String,
    val email: String,
    val status: String,
    val createdAt: String,
    val lastActive: String,
    val friends: Collection<Long>,
    val roles: Set<Role>
)
data class ShortUserVO (
    val id: Long,
    val username: String,
    val photoUrl: String,
    val lastActive: String
)

data class UserListVO (
    val users: List<UserVO>
)

data class ShortUserListVO (
    val friends: List<ShortUserVO>
)

data class MessageVO (
    val id: Long,
    val senderId: Long,
    val conversationId: Long?,
    val body: String?,
    val createdAt: String,
    val status: String
)

data class ConversationVO (
    @ApiModelProperty(notes = "Conversation identifier")
    val conversationId: Long?,
    @ApiModelProperty(notes = "List of conversation users")
    val users: List<ShortUserVO>,
    @ApiModelProperty(notes = "List of conversation messages")
    val messages: List<MessageVO>,
    val createdAt: String
)

data class ConversationListVO (
    val conversations: List<ConversationVO>
)