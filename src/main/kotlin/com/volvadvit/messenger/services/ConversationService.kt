package com.volvadvit.messenger.services

import com.volvadvit.messenger.exceptions.InvalidConversationException
import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.ConversationRepository
import org.springframework.stereotype.Service

@Service
class ConversationService(val repository: ConversationRepository) {

    fun createConversation(userA: User, userB: User): Conversation {
        val conversation = Conversation()
//        conversation.sender = userA
//        conversation.recipient = userB
        repository.save(conversation)
        return conversation
    }

    fun conversationExists(userA: User, userB: User): Boolean {
//        return if (repository.
//                    findBySenderIdAndRecipientId(userA.id, userB.id) != null)
//        true
//        else repository.findBySenderIdAndRecipientId(userB.id, userA.id) != null

        return true //заглушка
    }

    fun getConversation(userA: User, userB: User): Conversation? {
//        return when {
//            repository.findBySenderIdAndRecipientId(userA.id,
//                userB.id) != null -> {
//                repository.findBySenderIdAndRecipientId(userA.id, userB.id)}
//            repository.findBySenderIdAndRecipientId(userB.id,
//                userA.id) != null -> {
//                repository.findBySenderIdAndRecipientId(userB.id,
//                    userA.id)
//                }
//            else -> null

        return null // заглушка
    }

    fun retrieveThread(conversationId: Long): Conversation {
//        val conversation = repository.findById(conversationId)
//        if (conversation.isPresent) {
//            return conversation.get()
//        }
        throw InvalidConversationException("Invalid conversation id '$conversationId'")
    }
    fun listUserConversations(userId: Long): ArrayList<Conversation> {
//        val conversationList: ArrayList<Conversation> = ArrayList()
//        conversationList.addAll(repository.findBySenderId(userId))
//        conversationList.addAll(repository.findByRecipientId(userId))
//        return conversationList
        return arrayListOf() // заглушка
    }
    fun nameSecondUser(conversation: Conversation, userId: Long): String {
        // TODO заменить на возврат списка всех пользователей в беседе, кроме текущего
        val users = conversation.users
        while (users.iterator().hasNext()) {
            val user = users.iterator().next()
            if (user?.id != userId && user != null) {
                return user.username
            }
            continue
        }
        throw InvalidConversationException("List of users in conversation is empty")
    }
}