package ru.apexman.userservicekotlin.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import ru.apexman.userservicekotlin.IntegrationDataGenerator
import ru.apexman.userservicekotlin.SpringBootTestWithEmbeddedPostgres
import ru.apexman.userservicekotlin.dto.UserDto
import ru.apexman.userservicekotlin.repository.UserRepository

@SpringBootTestWithEmbeddedPostgres
class UserControllerTest(
    @Autowired
    private val testRestTemplate: TestRestTemplate,
    @Autowired
    private val userRepository: UserRepository,
) {

    @Test
    fun `get all users when empty`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity<String>(headers)
        val response = testRestTemplate.exchange(
            "/api/users", HttpMethod.GET, entity,
            object : ParameterizedTypeReference<Collection<UserDto>>() {}
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body?.isEmpty()!!)
    }

    @Test
    fun `get all users`() {
        IntegrationDataGenerator.createUser(userRepository, "1", "123")
        IntegrationDataGenerator.createUser(userRepository, "2", "asdf")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity<String>(headers)
        val response = testRestTemplate.exchange(
            "/api/users", HttpMethod.GET, entity,
            object : ParameterizedTypeReference<Collection<UserDto>>() {}
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body?.isNotEmpty()!!)
    }

    @Test
    fun `get user by id`() {
        val createdUser = IntegrationDataGenerator.createUser(userRepository, "1")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity<String>(headers)
        val response = testRestTemplate.exchange(
            "/api/users/${createdUser.id}", HttpMethod.GET, entity,
            UserDto::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(createdUser.id, response.body!!.id)
    }

    @Test
    fun `create user`() {
        val requestJson = """
            {
                "username": "hehe"
            }
        """.trimIndent()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val response = testRestTemplate.postForEntity(
            "/api/users",
            HttpEntity<String>(requestJson, headers),
            UserDto::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertNotNull(response.body)
    }

    @Test
    fun `create and get user`() {
        val requestJson = """
            {
                "username": "hehe"
            }
        """.trimIndent()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val response = testRestTemplate.postForEntity(
            "/api/users",
            HttpEntity<String>(requestJson, headers),
            UserDto::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertNotNull(response.body)

        val getUserByIdHeaders = HttpHeaders()
        getUserByIdHeaders.contentType = MediaType.APPLICATION_JSON
        val responseUserById = testRestTemplate.exchange(
            "/api/users/${response.body!!.id}", HttpMethod.GET, HttpEntity<String>(getUserByIdHeaders),
            UserDto::class.java
        )

        assertTrue(responseUserById.statusCode.is2xxSuccessful)
        assertEquals(response.body!!.id, responseUserById.body!!.id)
    }

    @Test
    fun `update user`() {
        val createdUser = IntegrationDataGenerator.createUser(userRepository, "1")

        val requestJson = """
            {
                "username": "another"
            }
        """.trimIndent()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity<String>(requestJson, headers)
        val response = testRestTemplate.exchange(
            "/api/users/${createdUser.id}", HttpMethod.PUT, entity,
            UserDto::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(createdUser.id, response.body!!.id)
        assertEquals("another", response.body!!.username)
    }

    @Test
    fun `delete user`() {
        val createdUser = IntegrationDataGenerator.createUser(userRepository, "1")
        val before = userRepository.findAll().size

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity<String>(headers)
        val response = testRestTemplate.exchange(
            "/api/users/${createdUser.id}", HttpMethod.DELETE, entity,
            UserDto::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(createdUser.id, response.body!!.id)
        val after = userRepository.findAll().size
        assertEquals(before - 1, after)
    }
}
