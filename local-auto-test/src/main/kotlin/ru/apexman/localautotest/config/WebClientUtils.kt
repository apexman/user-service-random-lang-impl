package ru.apexman.localautotest.config

import org.slf4j.Logger
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono

class WebClientUtils {

    companion object {

        fun logRequest(logger: Logger): ExchangeFilterFunction =
            ExchangeFilterFunction.ofRequestProcessor {
                logger.info("making request ${it.method().name}: ${it.url()}")
                Mono.just(it)
            }

        fun logResponse(logger: Logger): ExchangeFilterFunction =
            ExchangeFilterFunction.ofResponseProcessor {
                logger.info("received response with status code: ${it.statusCode()}")
                Mono.just(it)
            }
    }

}