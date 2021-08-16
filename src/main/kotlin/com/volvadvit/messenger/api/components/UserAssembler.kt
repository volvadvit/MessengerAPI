package com.volvadvit.messenger.api.components

import com.volvadvit.messenger.api.helpers.objects.UserListVO
import com.volvadvit.messenger.api.helpers.objects.UserVO
import com.volvadvit.messenger.api.models.User
import org.springframework.stereotype.Component

@Component
class UserAssembler {

    fun toUserVO(user: User) : UserVO {
        return UserVO(user.id, user.username, user.phoneNumber, user.status, user.createdAt.toString())
    }

    fun toUserListVO(users: List<User>) : UserListVO {
        val userVOList = users.map { toUserVO(it) }
        return UserListVO(userVOList)
    }
}