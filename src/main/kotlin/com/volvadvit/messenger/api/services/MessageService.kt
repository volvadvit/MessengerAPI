package com.volvadvit.messenger.api.services

import com.volvadvit.messenger.api.models.Message
import com.volvadvit.messenger.api.models.User

interface MessageService {
    fun sendMessage(sender: User, recipientId: Long, messageText: String) : Message
}