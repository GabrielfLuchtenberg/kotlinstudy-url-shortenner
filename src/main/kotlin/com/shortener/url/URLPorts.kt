package com.shortener.url

interface UrlCommands {
    suspend fun create(createShortenedUrl: CreateURL) : ShortenedUrl?
}

interface UrlQueries {
    suspend fun get(url: String): ShortenedUrl?
    suspend fun getAll(): List<ShortenedUrl>
}

class UrlCommandsImpl(private val urlRepository: UrlRepository): UrlCommands {

    override suspend fun create(createShortenedUrl: CreateURL) : ShortenedUrl? {
        val alreadyShortened = urlRepository.findByOriginalUrl(createShortenedUrl.urlToShort)
        alreadyShortened?.let {
            return ShortenedUrl(id = it.id, shortenedUrl = buildFullUrl(it.shortenedUrl), originalURL =  it.originalURL)
        }

        val randomString = createShort()
        val shortenedUrl = urlRepository.add(randomString, createShortenedUrl.urlToShort)

        return shortenedUrl?.let {
            ShortenedUrl(id = it.id, shortenedUrl = buildFullUrl(it.shortenedUrl), originalURL =  it.originalURL)
        }
    }
}

class UrlQueriesImpl(private val urlRepository: UrlRepository) : UrlQueries {
    override suspend fun get(url: String): ShortenedUrl? {
        val find = urlRepository.findByShortenedUrl(shortenedUrl = url)
        return find?.let {
            ShortenedUrl(id = it.id, shortenedUrl = buildFullUrl(it.shortenedUrl), originalURL = it.originalURL )
        }
    }
    override suspend fun getAll(): List<ShortenedUrl> {
        return urlRepository.all().map { ShortenedUrl(id = it.id, originalURL = it.originalURL, shortenedUrl = it.shortenedUrl) }
    }
}