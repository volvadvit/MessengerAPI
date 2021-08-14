package com.volvadvit.messenger.api.repositaries

import com.volvadvit.messenger.api.models.Conversation
import org.springframework.data.repository.CrudRepository

interface ConversationRepository : CrudRepository<Conversation, Long> {
    fun findBySenderId(id: Long) : List<Conversation>
    fun findByRecipientId(id: Long) : List<Conversation>
    fun findBySenderIdAndRecipientId(senderId: Long, recipientId: Long) : Conversation?
}