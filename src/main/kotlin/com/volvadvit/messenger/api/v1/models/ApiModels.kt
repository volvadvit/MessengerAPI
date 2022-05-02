package com.volvadvit.messenger.api.v1.models

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

data class MessageRequest(val recipientId: Long, val message: String)

@ApiModel("Common json format for all responses")
data class ResponseMapper (
    @ApiModelProperty("Status of response, like http status")
    val status: Int,
    @ApiModelProperty("Additional message with error/success message")
    val message: String,
    @ApiModelProperty("Response model of required entity")
    val body: Any
)

data class UserVO (
    val id: Long,
    val username: String,
    val phoneNumber: String,
    val status: String,
    val createdAt: String
)

@ApiModel(value = "Model to contain details for update user")
data class UpdatedUserDetails (
    val password: String?,
    val username: String?,
    val phoneNumber: String?,
    val status: String?
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
    @ApiModelProperty(notes = "Auto-generated identifier")
    val conversationId: Long?,
    @ApiModelProperty(notes = "Username of opposite user")
    val secondPartyUsername: String,
    @ApiModelProperty(notes = "List of messages in conversation")
    val messages: ArrayList<MessageVO>
)

data class ConversationListVO(
    val conversations: List<ConversationVO>
)