package com.volvadvit.messenger.api.services

import com.volvadvit.messenger.api.models.Conversation
import com.volvadvit.messenger.api.models.User

interface ConversationService {
    fun createConversation(userA: User, userB: User) : Conversation
    fun conversationExists(userA: User, userB: User) : Boolean
    fun getConversation(userA: User, userB: User): Conversation?
    fun retrieveThread(conversationId: Long) : Conversation
    fun listUserConversations(userId: Long) : List<Conversation>
    fun nameSecondParty(conversation: Conversation, userId: Long) : String
}