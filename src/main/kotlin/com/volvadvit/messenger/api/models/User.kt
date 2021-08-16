package com.volvadvit.messenger.api.models

import com.volvadvit.messenger.api.listeners.UserListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "user_details")
@EntityListeners(UserListener::class)
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @DateTimeFormat
    var createdAt: Date = Date.from(Instant.now()),

    @Column(unique = true)
    @Size(min = 2)
    var username: String = "",

    @Size(min = 11)
    @Pattern(regexp = "^\\(?(\\d{3})\\)?[-]?(\\d{3})[-]?(\\d{4})$")
    var phoneNumber: String = "",

    @Size(min = 60, max = 60)
    var password: String = "",

    var status: String = "available",

    @Pattern(regexp = "\\A(activated|deactivated)\\z")
    var accountStatus: String = "activated"
) {
    // sender messages collection
    @OneToMany(mappedBy = "sender", targetEntity = Message::class)
    private var sentMessages: Collection<Message>? = null

    // received messages collection
    @OneToMany(mappedBy = "recipient", targetEntity = Message::class)
    private var receivedMessages: Collection<Message>? = null
}