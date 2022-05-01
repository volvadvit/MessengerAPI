package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.models.ConversationListVO
import com.volvadvit.messenger.api.v1.models.ConversationVO
import com.volvadvit.messenger.api.v1.assemblers.ConversationAssembler
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.impl.ConversationServiceImpl
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/conversations")
@Api(value = "conversations")
class ConversationController(val conversationService: ConversationServiceImpl,
                             val conversationAssembler: ConversationAssembler, val userRepository: UserRepository
){

    @GetMapping
    @ApiOperation(value = "Get list of conversations for currently logged user")
    @ApiResponse(code = 200, message = "Return list of conversations", response = ConversationListVO::class)
    fun list(request: HttpServletRequest) : ResponseEntity<ConversationListVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name)
        val conversations = conversationService.listUserConversations(user!!.id)
        return ResponseEntity.ok(conversationAssembler.toConversationListVO(conversations, user.id))
    }

    @RequestMapping("/{conversation_id}", method = [RequestMethod.GET])
    fun show(@PathVariable(name = "conversation_id") conversationId: Long,
             request: HttpServletRequest) : ResponseEntity<ConversationVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name)
        val conversationThread = conversationService.retrieveThread(conversationId)
        return ResponseEntity.ok(conversationAssembler.toConversationVO(conversationThread, user!!.id))
    }
}