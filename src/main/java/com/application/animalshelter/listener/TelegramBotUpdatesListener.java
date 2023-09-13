package com.application.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TelegramBotUpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final MessageBuilder messageBuilder;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, MessageBuilder messageBuilder) {
        this.telegramBot = telegramBot;
        this.messageBuilder = messageBuilder;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    /**
     * Processes an update (user message).
     *
     * @param update Update object representing the update.
     */
    public void process(Update update) {
        String userName = update.message().chat().username();
        String userMessageText = update.message().text();
        long chatId = update.message().chat().id();

        if (update.message() == null) {
            return;
        }
        logger.info("Processing update: {}", update);
        sendMessage(chatId, userName, userMessageText);
    }

    /**
     * Sends a message to the user based on the textual command.
     *
     * @param chatId          Chat ID.
     * @param userName        User's name.
     * @param userMessageText User's message text.
     */
    private void sendMessage(long chatId, String userName, String userMessageText) {
        SendMessage message = switch (userMessageText) {
            case "/start" -> messageBuilder.getStartMessage(chatId, userName);
            case "Информация о боте" -> messageBuilder.getInfoMessage(chatId);
            case "Вернуться к выбору приюта" -> messageBuilder.getKeyboardShelterMessage(chatId);
            case "Приют для кошек", "Приют для собак" ->
                    messageBuilder.getKeyboardCommandsMessage(chatId, userMessageText);
            default -> new SendMessage(chatId, "Неизвестная команда: " + userMessageText);
        };
        try {
            telegramBot.execute(message);

        } catch (Exception e) {
            logger.error("Error sending message", e);
        }
    }
}
