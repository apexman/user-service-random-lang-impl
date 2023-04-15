package ru.apexman.userservicekotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.apexman.userservicekotlin.entity.Contact

interface ContactRepository : JpaRepository<Contact, Long> {

}

