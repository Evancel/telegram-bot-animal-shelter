package com.application.animalshelter.listener;


import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.entıty.AppUser;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final AppUserDAO appUserDAO;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);
            if(update.message()==null && update.callbackQuery()==null) {
                log.info("message = null");
            }
            if(update.message()!=null){
                switchMessages(update.message());
            }
            if(update.callbackQuery()!=null){
                switchCallback(update.callbackQuery());
            }
    });
    return CONFIRMED_UPDATES_ALL;
}
    private void switchMessages(Message message){
        log.info("switchMessages is processing...");
        User telegramUser = message.from();
        Long chatId = message.chat().id();

        if (message.text().equals("/start")) {
            if(appUserDAO.findByTelegramUserId(telegramUser.id())==null) {
                //кнопка стартового меню
                telegramBot.execute(new SetChatMenuButton().chatId(chatId)
//                    .menuButton(new MenuButtonWebApp("Menu", new WebAppInfo("https://core.telegram.org"))));
                        .menuButton(new MenuButton("/hello")));

                String helloText = "Привет, " + telegramUser.firstName() + "!\n" +
                        "Я помогу тебе забрать собаку или кошку домой из приюта Астаны. Для начала выбери животное:";

                //отправляем кнопки с вариантами приютов
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Приют для кошек  " + "\uD83D\uDC08")
                                .callbackData("catShelter"),
                        new InlineKeyboardButton("Приют для собак   " + "\uD83D\uDC15")
                                .callbackData("dogShelter"));

                SendMessage mess = new SendMessage(chatId, helloText);
                mess.replyMarkup(inlineKeyboard);
                telegramBot.execute(mess);

                AppUser tempAppUser = new AppUser();
                tempAppUser.setTelegramUserId(telegramUser.id());
                tempAppUser.setFirstName(telegramUser.firstName());
                tempAppUser.setLastName(telegramUser.lastName());
                tempAppUser.setUserName(telegramUser.username());
                tempAppUser.setActive(true);

                AppUser addedAppUser = appUserDAO.save(tempAppUser);
            } else{
                InlineKeyboardMarkup inlineKeyboard1 = new InlineKeyboardMarkup();
                inlineKeyboard1.addRow(new InlineKeyboardButton("Информация о приютах")
                        .callbackData("infoAboutShelter"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Как взять животное")
                        .callbackData("howTakeAnimal"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Прислать отчет о питомце")
                        .callbackData("sendReport"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Позвать волонтера")
                        .callbackData("callVolunteer"));

                SendMessage mess1 = new SendMessage(chatId,"Давай продолжим! Выбери, что тебя интересует:")
                        .replyMarkup(inlineKeyboard1);
                telegramBot.execute(mess1);
            }
        }
    }

    private void switchCallback(CallbackQuery callbackQuery){
        if(callbackQuery.data().contains("catShelter")||callbackQuery.data().contains("dogShelter")){
            log.info("callback_data = catShelter");

            InlineKeyboardMarkup inlineKeyboard1 = new InlineKeyboardMarkup();
            inlineKeyboard1.addRow(new InlineKeyboardButton("Информация о приютах")
                    .callbackData("infoAboutShelter"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Как взять животное")
                    .callbackData("howTakeAnimal"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Прислать отчет о питомце")
                    .callbackData("sendReport"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Позвать волонтера")
                    .callbackData("callVolunteer"));

            SendMessage mess1 = new SendMessage(callbackQuery.message().chat().id(),"Выбор меню")
                    .replyMarkup(inlineKeyboard1);
            telegramBot.execute(mess1);
        } else if(callbackQuery.data().contains("infoAboutShelter")){
            log.info("callback_data = infoAboutShelter");

            InlineKeyboardMarkup inlineKeyboard1 = new InlineKeyboardMarkup();
            inlineKeyboard1.addRow(new InlineKeyboardButton("Адреса приютов и время работы")
                    .callbackData("sheltersAddresses"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Оформление пропуска на машину")
                    .callbackData("passCar"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Правила нахождения внутри и общения с животным")
                    .callbackData("shelterRules"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Записать контактные данные для связи")
                    .callbackData("contactInformation"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Позвать волонтера")
                    .callbackData("callVolunteer"));

            SendMessage mess1 = new SendMessage(callbackQuery.message().chat().id(),"Я помогу тебе " +
                    "узнать информацию о приюте! Выбери, что тебя интересует: ")
                    .replyMarkup(inlineKeyboard1);
            telegramBot.execute(mess1);
        }
    }
}
