package com.shortener.url

import com.shortener.plugins.query
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

object UrlsTable : UUIDTable() {
    val shortenedUrl = varchar("shortened_url", 10).uniqueIndex()
    val originalURL =  varchar("original_url", 1024)
}

interface UrlRepository {
    suspend fun all(): List<ShortenedUrl>
    suspend fun findByShortenedUrl(shortenedUrl: String): ShortenedUrl?
    suspend fun findByOriginalUrl(originalURL: String): ShortenedUrl?
    suspend fun add(shortenedUrl: String, originalURL: String): ShortenedUrl?
}

class UrlRepositoryExposed : UrlRepository {
    private fun resultRowToUrl(row: ResultRow) = ShortenedUrl(
        id = row[UrlsTable.id].value,
        shortenedUrl = row[UrlsTable.shortenedUrl],
        originalURL = row[UrlsTable.originalURL])
    override suspend fun all(): List<ShortenedUrl> = query {
        UrlsTable.selectAll().map(::resultRowToUrl)
    }

    override suspend fun findByShortenedUrl(shortenedUrl: String): ShortenedUrl? = query {
        UrlsTable.select{UrlsTable.shortenedUrl eq shortenedUrl}.map(::resultRowToUrl).singleOrNull()
    }

    override suspend fun findByOriginalUrl(originalURL: String): ShortenedUrl? = query {
        UrlsTable.select {
            UrlsTable.originalURL eq originalURL
        }.map(::resultRowToUrl).singleOrNull()
    }

    override suspend fun add(shortenedUrl: String, originalURL: String): ShortenedUrl? = query {
        val insertStatement = UrlsTable.insert {
            it[UrlsTable.originalURL] = originalURL
            it[UrlsTable.shortenedUrl] = shortenedUrl
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUrl)
    }
}