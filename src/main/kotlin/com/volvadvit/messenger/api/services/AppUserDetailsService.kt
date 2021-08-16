package com.volvadvit.messenger.api.services

import com.volvadvit.messenger.api.repositaries.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class AppUserDetailsService(val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(p0: String?): UserDetails {
        val user = p0?.let { userRepository.findByUsername(it) } ?:
                    throw UsernameNotFoundException("A user with the username $p0 doesn't exists")
        return User(user.username, user.password, ArrayList<GrantedAuthority>())
    }
}