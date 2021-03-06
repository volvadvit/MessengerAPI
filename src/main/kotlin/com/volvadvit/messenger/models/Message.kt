package com.volvadvit.messenger.models

import com.volvadvit.messenger.constants.MessageStatus
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
class Message {

    //TODO add service-logic for change message status

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    var sender: User? = null

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
//    var recipient: User? = null

    @Size(min = 1, max = 255)
    var body: String = ""

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    var conversation: Conversation? = null

    @DateTimeFormat
    var createdAt: Timestamp = Timestamp.from(Instant.now())

    @Enumerated(EnumType.STRING)
    var status: MessageStatus = MessageStatus.CREATED
}