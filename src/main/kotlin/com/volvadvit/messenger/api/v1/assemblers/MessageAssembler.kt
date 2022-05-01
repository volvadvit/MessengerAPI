package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.MessageVO
import com.volvadvit.messenger.models.Message
import org.springframework.stereotype.Component

@Component
class MessageAssembler {

    fun toMessageVO(message: Message) : MessageVO {
        return MessageVO(message.id, message.sender!!.id, message.recipient!!.id,
            message.conversation!!.id, message.body, message.createdAt.toString()
        )
    }
}