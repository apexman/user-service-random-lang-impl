package ru.apexman.userservicekotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.apexman.userservicekotlin.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

}
