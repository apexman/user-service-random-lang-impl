package ru.apexman.userservicekotlin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.apexman.userservicekotlin.controller.errorhandling.ConflictException
import ru.apexman.userservicekotlin.controller.errorhandling.NotFoundException
import ru.apexman.userservicekotlin.dto.CreateUserRequest
import ru.apexman.userservicekotlin.dto.ModifyUserRequest
import ru.apexman.userservicekotlin.entity.User
import ru.apexman.userservicekotlin.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { NotFoundException("Could not find user with id $userId") }
    }

    fun createUser(
        request: CreateUserRequest
    ): User {
        if (userRepository.findByUsername(request.username) != null) {
            throw ConflictException("User with username ${request.username} is already registered")
        }
        val newUser = User(username = request.username)
        return userRepository.save(newUser)
    }

    fun modifyUser(userId: Long, request: ModifyUserRequest): User {
        val user =
            userRepository.findById(userId).orElseThrow { NotFoundException("Could not find user with id $userId") }
        request.username?.let {
            if (userRepository.findByUsername(it) != null) {
                throw ConflictException("User with username $it is already registered")
            }
            user.username = it
            userRepository.saveAndFlush(user)
        }
        return user
    }

    @Transactional
    fun deleteUser(userId: Long): User {
        val user =
            userRepository.findById(userId).orElseThrow { NotFoundException("Could not find user with id $userId") }
        userRepository.delete(user)
        return user
    }
}