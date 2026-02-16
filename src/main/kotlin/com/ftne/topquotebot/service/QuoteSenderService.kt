package com.ftne.topquotebot.service

import mu.KotlinLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class QuoteSenderService(
    private val jdbcTemplate: JdbcTemplate
) {

    // TODO: switch to streaming with r2dbc?
    @Scheduled(fixedDelay = 1000)
    fun sendRandomQuoteWithDelay() {
        /* List<String> jdbcTemplate.query("SELECT DISTINCT user_id FROM quotes")
         for (user: users)
            {
                jdbcTemplate (getRndQuote)
            }
         */
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}