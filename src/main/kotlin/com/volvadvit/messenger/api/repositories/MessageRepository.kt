package com.volvadvit.messenger.api.repositories

import com.volvadvit.messenger.api.models.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : CrudRepository<Message, Long> {
    fun findByConversationId(conversationId: Long) : List<Message>
}