package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.UserListVO
import com.volvadvit.messenger.api.v1.models.UserVO
import com.volvadvit.messenger.models.User
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