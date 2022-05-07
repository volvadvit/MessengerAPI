package com.volvadvit.messenger.models

import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToMany(mappedBy = "conversations", targetEntity = User::class)
    val users: Set<User?> = hashSetOf()

    @DateTimeFormat
    var createdAt: Timestamp = Timestamp.from(Instant.now())

    @OneToMany(mappedBy = "conversation", targetEntity = Message::class, cascade = [CascadeType.ALL])
    val messages: List<Message> = arrayListOf()

    var photoUrl: String? = null

    var name: String? = null
}