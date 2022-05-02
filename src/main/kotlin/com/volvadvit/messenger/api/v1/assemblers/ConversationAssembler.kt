package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.ConversationListVO
import com.volvadvit.messenger.api.v1.models.ConversationVO
import com.volvadvit.messenger.api.v1.models.MessageVO
import com.volvadvit.messenger.models.Conversation
import com.volvadvit.messenger.services.ConversationService
import org.springframework.stereotype.Component

@Component
class ConversationAssembler(val conversationService: ConversationService,
                            val messageAssembler: MessageAssembler
) {
    fun toConversationVO(conversation: Conversation, userId: Long) : ConversationVO {
        val conversationMessages: ArrayList<MessageVO> = ArrayList()
        conversation.messages!!.mapTo(conversationMessages) {
            messageAssembler.toMessageVO(it)
        }
        return ConversationVO(
            conversation.id,
            conversationService.nameSecondParty(conversation, userId), conversationMessages
        )
    }

    fun toConversationListVO(conversations: ArrayList<Conversation>, userId: Long)
    : ConversationListVO {
        val conversationVOList = conversations.map { toConversationVO(it, userId) }
        return ConversationListVO(conversationVOList)
    }
}