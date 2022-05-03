package com.volvadvit.messenger.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToMany(mappedBy = "conversations", targetEntity = User::class)
    val users: Collection<User?> = mutableListOf()

    @DateTimeFormat
    var createdAt: Date = Date.from(Instant.now())

    @OneToMany(mappedBy = "conversation", targetEntity = Message::class, cascade = [CascadeType.ALL])
    val messages: Collection<Message> = mutableListOf()

    var photoURL: String? = null

    var name: String? = null
}