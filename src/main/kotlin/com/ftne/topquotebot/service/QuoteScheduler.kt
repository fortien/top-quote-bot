package com.ftne.topquotebot.service

import com.ftne.topquotebot.bot.QuoteBot
import com.ftne.topquotebot.repository.QuoteDao
import com.ftne.topquotebot.repository.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class QuoteScheduler(
    private val userDao: UserDao,
    private val quoteDao: QuoteDao,
    private val telegramBot: QuoteBot
) {

    @Scheduled(cron = "0 * * * * *") // Каждую минуту
    fun sendRandomQuotes() {
        val users = jdbcTemplate.query(
            "SELECT chat_id FROM users"
        ) { rs, _ -> rs.getLong("chat_id") }

        users.forEach { chatId ->
            quoteDao.findRandomByUser(chatId)?.let { quote ->
                telegramBot.sendMessage(chatId, "Цитата дня:\n${quote.text}")
            }
        }
    }

    // Добавляем JdbcTemplate для прямой работы с пользователями
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
}