package com.shortener.url

import kotlinx.serialization.Serializable

@Serializable
data class CreateURL(val urlToShort: String)

@Serializable
data class URLCreated(val shortenedUrl: String, val originalURL: String)

class ShortenedUrl(val shortenedUrl: String, val originalURL: String)