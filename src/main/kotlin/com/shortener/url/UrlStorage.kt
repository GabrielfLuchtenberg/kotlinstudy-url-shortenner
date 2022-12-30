package com.shortener.url

import kotlin.random.Random

interface UrlStorage{
    fun store(createShortenedUrl: CreateURL) : URLCreated
    fun get(url: String): ShortenedUrl?
    fun getAll(): List<URLCreated>
}

class UrlStoragePG: UrlStorage {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var database = mutableListOf<ShortenedUrl>()
    private val baseUrl = "http://localhost:8080/"

    override fun store(createShortenedUrl: CreateURL) : URLCreated {

        val randomString = (1..8)
            .map { Random.nextInt(0, charPool.size)
                .let { charPool[it] } }.joinToString("")

        val shortenedUrl = ShortenedUrl(shortenedUrl = randomString, originalURL = createShortenedUrl.urlToShort)
        database.add(shortenedUrl)

        return URLCreated(shortenedUrl = baseUrl + shortenedUrl.shortenedUrl, originalURL =  shortenedUrl.originalURL)
    }

    override fun get(url: String): ShortenedUrl? {
        val find = database.find { shortenedUrl -> shortenedUrl.shortenedUrl == url }

        return find?.let {
            ShortenedUrl(shortenedUrl = baseUrl + it.shortenedUrl, originalURL = it.originalURL )
        }
    }

    override fun getAll(): List<URLCreated> {
        return database.map { URLCreated(shortenedUrl = it.shortenedUrl, originalURL = it.originalURL) }
    }
}