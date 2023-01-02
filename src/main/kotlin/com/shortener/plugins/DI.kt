package com.shortener.plugins

import com.shortener.url.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val mainModule = module {
    single <UrlRepository> {UrlRepositoryExposed()}
    single <UrlCommands> {UrlCommandsImpl(get())}
    single <UrlQueries> {UrlQueriesImpl(get())}
}

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(mainModule)
    }
}