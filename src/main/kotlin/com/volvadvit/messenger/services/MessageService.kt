package com.volvadvit.messenger.services

import com.volvadvit.messenger.exceptions.MessageEmptyException
import com.volvadvit.messenger.exceptions.InvalidMessageRecipientException
import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.models.Message
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.ConversationRepository
import com.volvadvit.messenger.repositories.MessageRepository
import com.volvadvit.messenger.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class MessageService (val repository: MessageRepository,
                      val conversationRepository: ConversationRepository,
                      val conversationService: ConversationService,
                      val userRepository: UserRepository
) {

    @Throws(MessageEmptyException::class, InvalidMessageRecipientException::class)
    fun sendMessage(sender: User, recipientId: Long, messageText: String): Message {
        val optional = userRepository.findById(recipientId)
        if (optional.isPresent) {
            val recipient = optional.get()
            if (messageText.isNotEmpty()) {
                val conversation: Conversation =
                    if (conversationService.conversationExists(sender, recipient)) {
                        conversationService.getConversation(sender, recipient) as Conversation
                    } else {
                        conversationService.createConversation(sender, recipient)
                    }
                conversationRepository.save(conversation)
                val message = Message()
                message.sender = sender
                message.body = messageText
                message.conversation = conversation
                repository.save(message)
                return message
            }
        } else {
            throw InvalidMessageRecipientException("The recipient id '$recipientId' is invalid")
        }
        throw MessageEmptyException()
    }
}