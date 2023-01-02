@file:UseSerializers(UUIDSerializer::class)

package com.shortener.url

import com.shortener.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*


@Serializable
data class CreateURL(val urlToShort: String)

@Serializable
data class ShortenedUrl(val id: UUID?,
                        val shortenedUrl: String,
                        val originalURL: String)