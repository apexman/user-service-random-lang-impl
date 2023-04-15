package ru.apexman.userservicekotlin.dto

data class UserDto(
    val id: Long,
    val username: String,
    val contacts: List<ContactDto>,
)
