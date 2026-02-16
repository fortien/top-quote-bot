package com.ftne.topquotebot.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant

data class User(
    val chatId: Long,
    val username: String?,
    val firstName: String?,
    val lastName: String?
)

data class Quote(
    val id: Long,
    val text: String,
    val timestamp: Instant,
    val userChatId: Long
)

@Repository
class UserDao(private val jdbcTemplate: JdbcTemplate) {

    fun create(user: User) {
        jdbcTemplate.update(
            """
            INSERT INTO users (chat_id, username, first_name, last_name)
            VALUES (?, ?, ?, ?)
            """.trimIndent(),
            user.chatId, user.username, user.firstName, user.lastName
        )
    }

    fun findById(chatId: Long): User? {
        return jdbcTemplate.queryForObject(
            """
            SELECT chat_id, username, first_name, last_name 
            FROM users WHERE chat_id = ?
            """.trimIndent(),
            { rs, _ ->
                User(
                    rs.getLong("chat_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                )
            },
            chatId
        )
    }
}

@Repository
class QuoteDao(private val jdbcTemplate: JdbcTemplate) {

    fun create(quote: Quote): Long {
        return jdbcTemplate.queryForObject(
            """
            INSERT INTO quotes (text, timestamp, user_chat_id)
            VALUES (?, ?, ?)
            RETURNING id
            """.trimIndent(),
            Long::class.java,
            quote.text, Timestamp.from(quote.timestamp), quote.userChatId
        ) ?: throw IllegalStateException("Failed to insert quote")
    }

    fun delete(quoteId: Long, userChatId: Long): Boolean {
        return jdbcTemplate.update(
            """
            DELETE FROM quotes 
            WHERE id = ? AND user_chat_id = ?
            """.trimIndent(),
            quoteId, userChatId
        ) > 0
    }

    fun findRandomByUser(chatId: Long): Quote? {
        return jdbcTemplate.query(
            """
            SELECT id, text, timestamp, user_chat_id
            FROM quotes
            WHERE user_chat_id = ?
            ORDER BY RANDOM()
            LIMIT 1
            """.trimIndent(),
            { rs, _ ->
                Quote(
                    rs.getLong("id"),
                    rs.getString("text"),
                    rs.getTimestamp("timestamp").toInstant(),
                    rs.getLong("user_chat_id")
                )
            },
            chatId
        ).firstOrNull()
    }

    fun findAllByUser(chatId: Long): List<Quote> {
        return jdbcTemplate.query(
            """
            SELECT id, text, timestamp, user_chat_id 
            FROM quotes 
            WHERE user_chat_id = ?
            """.trimIndent(),
            { rs, _ ->
                Quote(
                    rs.getLong("id"),
                    rs.getString("text"),
                    rs.getTimestamp("timestamp").toInstant(),
                    rs.getLong("user_chat_id")
                )
            },
            chatId
        )
    }
}