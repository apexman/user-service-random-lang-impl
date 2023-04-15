package ru.apexman.localautotest.config

import io.netty.handler.logging.LogLevel
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat
import ru.apexman.localautotest.config.WebClientUtils.Companion.logRequest
import ru.apexman.localautotest.config.WebClientUtils.Companion.logResponse


@Configuration
class KotlinAppWebClientConfig(
) {
    private val logger = LoggerFactory.getLogger(KotlinAppWebClientConfig::class.java)

    @Bean
    fun kotlinAppWebClient(testingAppsConfig: TestingAppsConfig): WebClient {
        val factory = DefaultUriBuilderFactory(testingAppsConfig.kotlinAppUrl + testingAppsConfig.userServiceUri)
        factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        val httpClient = HttpClient.create()
            .wiretap(this.javaClass.canonicalName, LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
        return WebClient
            .builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl(testingAppsConfig.kotlinAppUrl + testingAppsConfig.userServiceUri)
            .uriBuilderFactory(factory)
            .filters { exchangeFilterFunctions ->
                exchangeFilterFunctions.add(logRequest(logger))
                exchangeFilterFunctions.add(logResponse(logger))
            }
            .build()
    }
}