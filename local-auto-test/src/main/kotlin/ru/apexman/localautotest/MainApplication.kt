package ru.apexman.localautotest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.apexman.localautotest.config.TestingAppsConfig

@EnableConfigurationProperties(
    value = [TestingAppsConfig::class]
)
@SpringBootApplication
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

