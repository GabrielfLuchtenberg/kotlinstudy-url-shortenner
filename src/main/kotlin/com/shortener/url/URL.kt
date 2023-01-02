package com.shortener.url

import kotlin.random.Random

fun buildFullUrl(short: String): String {
    val baseUrl = "http://localhost:8080/"
    return baseUrl + short
}

fun createShort(): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..8)
        .map { Random.nextInt(0, charPool.size)
            .let { charPool[it] } }.joinToString("")
}