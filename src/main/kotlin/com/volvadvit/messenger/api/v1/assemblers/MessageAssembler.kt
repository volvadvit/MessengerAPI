package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.MessageVO
import com.volvadvit.messenger.exceptions.InvalidMessageException
import com.volvadvit.messenger.models.Message
import org.springframework.stereotype.Component

@Component
class MessageAssembler {

    fun toMessageVO(message: Message) : MessageVO {
        return MessageVO(
            message.id,
            message.sender?.id ?: throw InvalidMessageException("Message sender not found"),
            message.conversation?.id ?: throw InvalidMessageException("Not found conversation for message"),
            message.body,
            message.createdAt.toString(),
            message.status.name
        )
    }
}