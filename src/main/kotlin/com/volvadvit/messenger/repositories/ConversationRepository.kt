package com.volvadvit.messenger.repositories

import com.volvadvit.messenger.models.Conversation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationRepository : CrudRepository<Conversation, Long> {
    fun findBySenderId(id: Long) : List<Conversation>
    fun findByRecipientId(id: Long) : List<Conversation>
    fun findBySenderIdAndRecipientId(senderId: Long, recipientId: Long) : Conversation?
}