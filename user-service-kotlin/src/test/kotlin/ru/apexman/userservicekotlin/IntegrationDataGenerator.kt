package ru.apexman.userservicekotlin

import ru.apexman.userservicekotlin.entity.Contact
import ru.apexman.userservicekotlin.entity.User
import ru.apexman.userservicekotlin.repository.UserRepository

class IntegrationDataGenerator {

    companion object {
        fun createUser(
            userRepository: UserRepository,
            username: String = "Test Provider",
            contactValue: String = "test provider telegram",
        ): User {
            val user = User(
                username = username,
                contacts = mutableListOf(),
            )
            val contact = Contact(user, contactValue, "ContactType.TELEGRAM")
            user.contacts.add(contact)
            return userRepository.save(user)
        }
    }

}
