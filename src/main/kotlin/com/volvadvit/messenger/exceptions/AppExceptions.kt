package com.volvadvit.messenger.exceptions

class UsernameUnavailableException(override val message: String) : RuntimeException()
class UserNotExists(override val message: String) : RuntimeException()
class UserAlreadyExists(override val message: String): RuntimeException()
class InvalidUserIdException(override val message: String) : RuntimeException()
class MessageEmptyException(override val message: String = "A message cannot be empty") : RuntimeException()
class InvalidMessageRecipientException(override val message: String) : RuntimeException()
class InvalidConversationException(override val message: String) : RuntimeException()
class InvalidMessageException(override val message: String) : RuntimeException()
class UserDeactivatedException(override val message: String) : RuntimeException()
class InvalidTokenException(override val message: String) : RuntimeException()
