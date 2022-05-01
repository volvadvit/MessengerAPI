package com.volvadvit.messenger.services.impl

import com.volvadvit.messenger.exceptions.MessageEmptyException
import com.volvadvit.messenger.exceptions.MessageRecipientInvalidException
import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.models.Message
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.ConversationRepository
import com.volvadvit.messenger.repositories.MessageRepository
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.ConversationService
import com.volvadvit.messenger.services.MessageService
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl (val repository: MessageRepository,
                          val conversationRepository: ConversationRepository,
                          val conversationService: ConversationService,
                          val userRepository: UserRepository
) : MessageService {

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