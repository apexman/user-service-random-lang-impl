package ru.apexman.localautotest.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "testing-apps")
data class TestingAppsConfig(
    val userServiceUri: String,
    val kotlinAppUrl: String
)
