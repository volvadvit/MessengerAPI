package com.volvadvit.messenger.models

import com.volvadvit.messenger.repositories.listeners.UserListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "user_details")
@EntityListeners(UserListener::class)
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @DateTimeFormat
    var createdAt: Date = Date.from(Instant.now())

    @Column(unique = true)
    @Size(min = 2)
    var username: String = ""

    @Pattern(regexp = "^\\(?(\\d{3})\\)?[-]?(\\d{3})[-]?(\\d{4})$")
    var phoneNumber: String = ""

    @Size(min = 3, max = 60)
    var password: String = ""

    var status: String = "available"

    @Pattern(regexp = "\\A(activated|deactivated)\\z")
    var accountStatus: String = "activated"

    // sender messages collection
    @OneToMany(mappedBy = "sender", targetEntity = Message::class, fetch = FetchType.LAZY)
    var sentMessages: Collection<Message>? = null

    // received messages collection
    @OneToMany(mappedBy = "recipient", targetEntity = Message::class, fetch = FetchType.LAZY)
    var receivedMessages: Collection<Message>? = null

    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(
        EnumType.STRING
    )
    var roles: Set<Role>? = null
}