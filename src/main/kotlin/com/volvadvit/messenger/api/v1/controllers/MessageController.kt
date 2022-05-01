package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.models.MessageRequest
import com.volvadvit.messenger.api.v1.models.MessageVO
import com.volvadvit.messenger.api.v1.assemblers.MessageAssembler
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.impl.MessageServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/messages")
class MessageController(val messageService: MessageServiceImpl,
                        val userRepository: UserRepository, val messageAssembler: MessageAssembler
) {

    @PostMapping
    fun create(@RequestBody messageDetails: MessageRequest, request: HttpServletRequest) : ResponseEntity<MessageVO> {
        val principal = request.userPrincipal
        val sender = userRepository.findByUsername(principal.name)
            val message = messageService.sendMessage(sender!!, messageDetails.recipientId, messageDetails.message)
        return ResponseEntity.ok(messageAssembler.toMessageVO(message))
    }
}