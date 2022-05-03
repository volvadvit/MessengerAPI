package com.volvadvit.messenger.api.v1.assemblers

import com.volvadvit.messenger.api.v1.models.ShortUserVO
import com.volvadvit.messenger.api.v1.models.UserListVO
import com.volvadvit.messenger.api.v1.models.UserVO
import com.volvadvit.messenger.exceptions.InvalidUserException
import com.volvadvit.messenger.models.User
import org.springframework.stereotype.Component

@Component
class UserAssembler {

    fun toUserVO(user: User) : UserVO {
        return UserVO(
            user.id,
            user.username,
            user.email,
            user.status,
            user.lastActive.toString(),
            user.createdAt.toString(),
            user.lastActive.toString(),
            user.friends.map { toShortUserVO(it)},
            user.roles ?: throw InvalidUserException("Cannot return user without role"))
    }

    fun toUserListVO(users: List<User>) : UserListVO {
        return UserListVO(users.map { toUserVO(it) })
    }

    fun toShortUserVO(user: User) : ShortUserVO {
        return ShortUserVO(
            user.id,
            user.username,
            user.photoUrl ?: "https://i.ytimg.com/vi/dqQcGplC2eg/mqdefault.jpg",
            user.lastActive.toString()
        )
    }

    fun toShortUserListVO(users: List<User>) : List<ShortUserVO> {
        return users.map { toShortUserVO(it) }
    }
}