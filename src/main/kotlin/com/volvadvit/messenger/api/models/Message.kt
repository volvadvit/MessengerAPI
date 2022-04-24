package com.volvadvit.messenger.api.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    var sender: User? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    var recipient: User? = null

    var body: String? = ""

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    var conversation: Conversation? = null

    @DateTimeFormat
    var createdAt: Date = Date.from(Instant.now())
}