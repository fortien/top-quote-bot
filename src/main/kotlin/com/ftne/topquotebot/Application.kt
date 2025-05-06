package com.ftne.topquotebot

import com.ftne.topquotebot.config.BotProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableConfigurationProperties(BotProperties::class)
@EnableScheduling
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}