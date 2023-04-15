package ru.apexman.userservicekotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import ru.apexman.userservicekotlin.dto.ContactDto

@Entity
@Table(name = "contacts")
class Contact(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    var contactValue: String,
    val contactType: String
) : AuditingEntityWithLongKey() {
    fun toDto(): ContactDto {
        return ContactDto(
            id = id!!,
            value = contactValue,
            type = contactType,
        )
    }
}
