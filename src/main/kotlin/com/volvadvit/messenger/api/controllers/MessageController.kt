package com.volvadvit.messenger.api.controllers

import com.volvadvit.messenger.api.components.MessageAssembler
import com.volvadvit.messenger.api.helpers.objects.MessageVO
import com.volvadvit.messenger.api.repositories.UserRepository
import com.volvadvit.messenger.api.services.impl.MessageServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/messages")
class MessageController(val messageService: MessageServiceImpl,
                        val userRepository: UserRepository, val messageAssembler: MessageAssembler) {

    @PostMapping
    fun create(@RequestBody messageDetails: MessageRequest, request: HttpServletRequest) : ResponseEntity<MessageVO> {
        val principal = request.userPrincipal
        val sender = userRepository.findByUsername(principal.name)
            val message = messageService.sendMessage(sender!!, messageDetails.recipientId, messageDetails.message)
        return ResponseEntity.ok(messageAssembler.toMessageVO(message))
    }

    data class MessageRequest(val recipientId: Long, val message: String)
}