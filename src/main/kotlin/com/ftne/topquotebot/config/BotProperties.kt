package com.ftne.topquotebot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "bot")
data class BotProperties @ConstructorBinding constructor(
    val name: String,
    val token: String,
    val adminChatId: String
)