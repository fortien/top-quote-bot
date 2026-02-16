package com.ftne.topquotebot.bot

import com.ftne.topquotebot.service.QuoteService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class QuoteBot(
    @Value("\${telegram.bot.token}") private val token: String,
    private val quoteService: QuoteService
) : TelegramLongPollingBot(token) {

    override fun getBotUsername(): String = "QuotesBot"

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val message = update.message
            val chatId = message.chatId
            val text = message.text

            when {
                text.startsWith("/addquote") -> handleAddQuote(chatId, text)
                text.startsWith("/deletequote") -> handleDeleteQuote(chatId, text)
                text.startsWith("/myquotes") -> handleMyQuotes(chatId)
                else -> sendMessage(chatId, "Неизвестная команда")
            }
        }
    }

    private fun handleAddQuote(chatId: Long, text: String) {
        val quoteText = text.removePrefix("/addquote").trim()
        if (quoteText.isEmpty()) {
            sendMessage(chatId, "Введите цитату после команды /addquote")
            return
        }
        quoteService.addQuote(chatId, quoteText)
        sendMessage(chatId, "Цитата добавлена!")
    }

    private fun handleDeleteQuote(chatId: Long, text: String) {
        val quoteId = text.removePrefix("/deletequote").trim().toLongOrNull()
            ?: run {
                sendMessage(chatId, "Введите ID цитаты после команды /deletequote")
                return
            }

        if (quoteService.deleteQuote(chatId, quoteId)) {
            sendMessage(chatId, "Цитата удалена")
        } else {
            sendMessage(chatId, "Цитата не найдена или нет прав для удаления")
        }
    }

    private fun handleMyQuotes(chatId: Long) {
        val quotes = quoteService.getAllUserQuotes(chatId)
        if (quotes.isEmpty()) {
            sendMessage(chatId, "У вас нет сохраненных цитат")
            return
        }
        val message = quotes.joinToString("\n\n") { "ID: ${it.id}\n${it.text}" }
        sendMessage(chatId, "Ваши цитаты:\n$message")
    }

    fun sendMessage(chatId: Long, text: String) {
        execute(SendMessage(chatId.toString(), text))
    }
}