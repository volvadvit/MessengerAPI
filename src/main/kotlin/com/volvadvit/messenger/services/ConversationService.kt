package com.volvadvit.messenger.services

import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.models.User

interface ConversationService {
    fun createConversation(userA: User, userB: User) : Conversation
    fun conversationExists(userA: User, userB: User) : Boolean
    fun getConversation(userA: User, userB: User): Conversation?
    fun retrieveThread(conversationId: Long) : Conversation
    fun listUserConversations(userId: Long) : List<Conversation>
    fun nameSecondParty(conversation: Conversation, userId: Long) : String
}