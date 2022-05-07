package com.volvadvit.messenger.constants

enum class ResponseConstants(val value: String) {
    USERNAME_UNAVAILABLE("User is disabled or not found"),
    INVALID_TOKEN("Token have bad format or null"),
    MESSAGE_EMPTY("Message is empty"),
    MESSAGE_RECIPIENT_INVALID("Message recipient is disabled or not found"),
    ACCOUNT_DEACTIVATED("User's account is deactivated"),
    INVALID_CONVERSATION_ID("Conversation not found for provided id"),
    INVALID_MESSAGE_ID("Message not found for provided id"),
    ILLEGAL_PASSWORD("Password must contains more than 3 symbols"),
    SAVE_NULL_USER("Cannot save user without details"),
    USER_NOT_EXISTS("Not found user for provided username of id"),
    ID_NULL("Expect id but found null")
}