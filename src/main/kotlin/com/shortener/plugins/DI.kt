package com.shortener.plugins

import com.shortener.url.UrlStorage
import com.shortener.url.UrlStoragePG
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val mainModule = module {
    single <UrlStorage> {UrlStoragePG()}
}

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(mainModule)
    }
}