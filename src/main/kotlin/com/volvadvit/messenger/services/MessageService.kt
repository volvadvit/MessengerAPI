package com.volvadvit.messenger.services

import com.volvadvit.messenger.models.Message
import com.volvadvit.messenger.models.User

interface MessageService {
    fun sendMessage(sender: User, recipientId: Long, messageText: String) : Message
}