package com.volvadvit.messenger.api.services.impl

import com.volvadvit.messenger.api.exceptions.MessageEmptyException
import com.volvadvit.messenger.api.exceptions.MessageRecipientInvalidException
import com.volvadvit.messenger.api.models.Conversation
import com.volvadvit.messenger.api.models.Message
import com.volvadvit.messenger.api.models.User
import com.volvadvit.messenger.api.repositories.ConversationRepository
import com.volvadvit.messenger.api.repositories.MessageRepository
import com.volvadvit.messenger.api.repositories.UserRepository
import com.volvadvit.messenger.api.services.ConversationService
import com.volvadvit.messenger.api.services.MessageService
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl (val repository: MessageRepository,
                          val conversationRepository: ConversationRepository,
                          val conversationService: ConversationService,
                          val userRepository: UserRepository) : MessageService {

    @Throws(MessageEmptyException::class, MessageRecipientInvalidException::class)
    override fun sendMessage(sender: User, recipientId: Long, messageText: String): Message {
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
                message.recipient = recipient
                message.body = messageText
                message.conversation = conversation
                repository.save(message)
                return message
            }
        } else {
            throw MessageRecipientInvalidException("The recipient id '$recipientId' is invalid")
        }
        throw MessageEmptyException()
    }
}