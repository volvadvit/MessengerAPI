package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.ConversationListVO
import com.volvadvit.messenger.api.v1.models.ConversationVO
import com.volvadvit.messenger.api.v1.models.MessageVO
import com.volvadvit.messenger.exceptions.InvalidConversationException
import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.services.ConversationService
import org.springframework.stereotype.Component

@Component
class ConversationAssembler(val userAssembler: UserAssembler,
                            val messageAssembler: MessageAssembler
) {
    fun toConversationVO(conversation: Conversation, userId: Long) : ConversationVO {
        return ConversationVO(
            conversation.id,
            conversation.users.map { userAssembler.toShortUserVO(it!!) },
            conversation.messages.map { messageAssembler.toMessageVO(it) }
        )
    }

    fun toConversationListVO(conversations: ArrayList<Conversation>, userId: Long)
    : ConversationListVO {
        val conversationVOList = conversations.map { toConversationVO(it, userId) }
        return ConversationListVO(conversationVOList)
    }
}