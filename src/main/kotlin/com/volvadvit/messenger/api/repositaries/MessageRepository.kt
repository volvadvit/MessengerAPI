package com.volvadvit.messenger.api.repositaries

import com.volvadvit.messenger.api.models.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, Long> {
    fun findByConversationId(conversationId: Long) : List<Message>
}