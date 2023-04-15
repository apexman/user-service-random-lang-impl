package ru.apexman.userservicekotlin.entity

import jakarta.persistence.*
import ru.apexman.userservicekotlin.dto.UserDto

@Entity
@Table(name = "users")
class User(
    var username: String,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val contacts: MutableCollection<Contact> = mutableListOf(),
) : AuditingEntityWithLongKey() {
    fun toDto(): UserDto {
        return UserDto(
            id = id!!,
            username = username,
            contacts = contacts.map { it.toDto() }
        )
    }
}
