package ru.apexman.userservicekotlin.controller.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.apexman.userservicekotlin.dto.CreateUserRequest
import ru.apexman.userservicekotlin.dto.ModifyUserRequest
import ru.apexman.userservicekotlin.dto.UserDto
import ru.apexman.userservicekotlin.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    val userService: UserService,
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(userService.getAll().map { it.toDto() })
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUserById(userId).toDto())
    }

    @PostMapping
    fun createUser(
        @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.createUser(request).toDto())
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: ModifyUserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.modifyUser(userId, request).toDto())
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.deleteUser(userId).toDto())
    }

}
