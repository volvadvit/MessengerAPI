package com.volvadvit.messenger.api.components

import com.volvadvit.messenger.api.helpers.objects.MessageVO
import com.volvadvit.messenger.api.models.Message
import org.springframework.stereotype.Component

@Component
class MessageAssembler {

    fun toMessageVO(message: Message) : MessageVO {
        return MessageVO(message.id, message.sender!!.id, message.recipient!!.id,
            message.conversation!!.id, message.body, message.createdAt.toString()
        )
    }
}