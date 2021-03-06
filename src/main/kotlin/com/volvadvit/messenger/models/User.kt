package com.volvadvit.messenger.models

import com.volvadvit.messenger.constants.Role
import com.volvadvit.messenger.repositories.listeners.UserListener
import org.hibernate.validator.constraints.URL
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "user_details")
@EntityListeners(UserListener::class)
class User {

    //TODO loginAttemptsCount + email verification

    //TODO change 'var' to 'val' in all entities

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @DateTimeFormat
    var createdAt: Date = Timestamp.from(Instant.now())

    @Column(unique = true)
    @Size(min = 2)
    var username: String = ""

//    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    @Email
    var email: String = ""

    @Size(min = 3, max = 60)
    var password: String = ""

    @DateTimeFormat
    var lastActive: Timestamp = Timestamp.from(Instant.now())

    @Pattern(regexp = "\\A(activated|deactivated)\\z")
    var status: String = "activated"

    @ManyToMany
    @JoinTable(
        name = "user_conversations",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "conversation_id")])
    var conversations : Set<Conversation> = hashSetOf()

    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = setOf()

    @ElementCollection
    @CollectionTable(
        name = "user_friends",
        joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "friend_id")
    var friendsId: Set<Long> = hashSetOf()

    @URL
    var photoUrl: String? = null

    // sender messages collection
    @OneToMany(mappedBy = "sender", targetEntity = Message::class, fetch = FetchType.LAZY)
    var sentMessages: List<Message> = arrayListOf()

    override fun toString(): String {
        return "id=${this.id}, username=${this.username}, email=${this.email},photo=${this.photoUrl}"
    }

    // received messages collection
//    @OneToMany(mappedBy = "recipient", targetEntity = Message::class, fetch = FetchType.LAZY)
//    var receivedMessages: Collection<Message>? = null
}