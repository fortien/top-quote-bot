package com.ftne.topquotebot.bot

import com.ftne.topquotebot.config.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class QuoteBot(
    @Autowired private val botProperties: BotProperties
): TelegramLongPollingBot() {

    override fun getBotUsername(): String {
        return botProperties.name
    }

    // how to realise subscriptions
    override fun onUpdateReceived(update: Update) {
        // crud quote
        val message: Message = update.message
        if (update.message == null) {
            // log.error("Received invalid update with empty msg {}", update)
            println("Received invalid update with empty msg $update")
        }
        val chatId = message.chatId.toString()
        // log.atDebug().log("Chat {} : Received message {}", chatId, message.text)
        // log.atTrace().log("Chat {} : Message object: {}", chatId, message)
        val userMsg = message.text
        if (userMsg.startsWith("/new")) {
            val quote = userMsg.substring(5)
            // save quote
        }
        val text = "123"
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId
        sendMessage.text = text
        try {
            execute(sendMessage)
        } catch (ex: TelegramApiException) {
            //log.error("Error while sending text message", ex)
            //sendProcessingError(chatId, ex)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getBotToken(): String {
        return botProperties.token
    }
}