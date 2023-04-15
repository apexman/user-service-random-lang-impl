package ru.apexman.userservicekotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EnableConfigurationProperties()
@EnableJpaRepositories(basePackages = ["ru.apexman.userservicekotlin.repository"])
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

