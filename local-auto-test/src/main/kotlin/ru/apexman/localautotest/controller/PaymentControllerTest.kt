package ru.apexman.localautotest.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest(
    @Autowired val yCAuthWebClient: WebClient,
) {

    @Test
    fun `get all users`() {
        val response = yCAuthWebClient
            .get()
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Collection<Any>>() {})
            .block()

        assertNotNull(response!!.body)
        assertTrue(response.statusCode.is2xxSuccessful)
    }

    @Test
    fun `create user`() {
        val requestJson = """
            {
                "username": "${generateRandomName()}"
            }
        """.trimIndent()

        val response = yCAuthWebClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(requestJson.length.toLong())
            .bodyValue(requestJson)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(response!!.body)
        assertTrue(response.statusCode.is2xxSuccessful)
    }

    @Test
    fun `create and get user by id`() {
        val requestJson = """
            {
                "username": "${generateRandomName()}"
            }
        """.trimIndent()

        val response = yCAuthWebClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(requestJson.length.toLong())
            .bodyValue(requestJson)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(response!!.body)
        assertTrue(response.statusCode.is2xxSuccessful)

        val id = (response.body as LinkedHashMap<*, *>)["id"]
        val responseUserById = yCAuthWebClient
            .get()
            .uri("/$id")
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(responseUserById!!.body)
        assertTrue(responseUserById.statusCode.is2xxSuccessful)
    }

    @Test
    fun `create and update user`() {
        val createRequest = """
            {
                "username": "${generateRandomName()}"
            }
        """.trimIndent()

        val createdUserResponse = yCAuthWebClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(createRequest.length.toLong())
            .bodyValue(createRequest)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(createdUserResponse!!.body)
        assertTrue(createdUserResponse.statusCode.is2xxSuccessful)

        val id = (createdUserResponse.body as LinkedHashMap<*, *>)["id"]

        val newName = generateRandomName()
        val updateRequest = """
            {
                "username": "$newName"
            }
        """.trimIndent()
        val responseUserById = yCAuthWebClient
            .put()
            .uri("/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(updateRequest.length.toLong())
            .bodyValue(updateRequest)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(responseUserById!!.body)
        assertTrue(responseUserById.statusCode.is2xxSuccessful)
        val username = (responseUserById.body as LinkedHashMap<*, *>)["username"]
        Assertions.assertEquals(newName, username)
    }

    @Test
    fun `create and delete user`() {
        val createRequest = """
            {
                "username": "${generateRandomName()}"
            }
        """.trimIndent()

        val createdUserResponse = yCAuthWebClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(createRequest.length.toLong())
            .bodyValue(createRequest)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(createdUserResponse!!.body)
        assertTrue(createdUserResponse.statusCode.is2xxSuccessful)

        val id = (createdUserResponse.body as LinkedHashMap<*, *>)["id"]

        val responseUserById = yCAuthWebClient
            .delete()
            .uri("/$id")
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .block()

        assertNotNull(responseUserById!!.body)
        assertTrue(responseUserById.statusCode.is2xxSuccessful)
    }

    private fun generateRandomName(): String = UUID.randomUUID().toString().replace("-", "")
}
