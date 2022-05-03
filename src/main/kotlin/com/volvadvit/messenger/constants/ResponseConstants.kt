package com.volvadvit.messenger.constants

enum class ResponseConstants(val value: String) {
    SUCCESS("success"), ERROR("error"),
    USERNAME_UNAVAILABLE("User is disabled or not found"),
    INVALID_USER_ID("User not found for provided id"),
    MESSAGE_EMPTY("Message is empty"),
    MESSAGE_RECIPIENT_INVALID("Message recipient is disabled or not found"),
    ACCOUNT_DEACTIVATED("User's account is deactivated"),
    INVALID_CONVERSATION_ID("Conversation not found for provided id"),
    INVALID_MESSAGE_ID("Message not found for provided id")
}