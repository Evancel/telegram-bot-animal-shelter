package com.application.animalshelter.listener;


import com.application.animalshelter.dao.AnimalShelterDAO;
import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.dao.CurrChoiceDAO;
import com.application.animalshelter.entity.AnimalShelter;
import com.application.animalshelter.entity.AppUser;
import com.application.animalshelter.entity.CurrChoice;
import com.application.animalshelter.enums.City;
import com.application.animalshelter.enums.UserCommand;
import com.application.animalshelter.service.AnimalShelterService;
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

import java.time.LocalDateTime;
import java.util.*;

/**
 * main class that processing the user's commands from telegram_bot
 */

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final AppUserDAO appUserDAO;
    private final AnimalShelterDAO animalShelterDAO;
    private final CurrChoiceDAO currChoiceDAO;
    private final AnimalShelterService animalShelterService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(AppUserDAO appUserDAO, AnimalShelterDAO animalShelterDAO, CurrChoiceDAO currChoiceDAO, AnimalShelterService animalShelterService) {
        this.appUserDAO = appUserDAO;
        this.animalShelterDAO = animalShelterDAO;
        this.currChoiceDAO = currChoiceDAO;
        this.animalShelterService = animalShelterService;
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

    /**
     * Method that contains logic of processing messages <br>
     * based on: {@link TelegramBot} <br>
     * using {@code InlineKeyboardMarkup}
     * @see TelegramBot
     */
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

                String helloText = "Hello, " + telegramUser.firstName() + "!\n" +
                        "I will help you to take care of a cat or a dog from a shelter in Astana. " +
                        "To start choose an animal: ";

                //отправляем кнопки с вариантами приютов
                ArrayList<UserCommand> commandArrayList = new ArrayList<>();
                commandArrayList.add(UserCommand.CAT_SHELTER);
                commandArrayList.add(UserCommand.DOG_SHELTER);

                sendMessage(chatId,helloText,createInlineKeyboard(commandArrayList));

                AppUser tempAppUser = new AppUser();
                tempAppUser.setTelegramUserId(telegramUser.id());
                tempAppUser.setFirstName(telegramUser.firstName());
                tempAppUser.setLastName(telegramUser.lastName());
                tempAppUser.setUserName(telegramUser.username());
                tempAppUser.setActive(true);

                AppUser addedAppUser = appUserDAO.save(tempAppUser);
            } else{
                ArrayList<UserCommand> commandArrayList = new ArrayList<>();
                commandArrayList.add(UserCommand.INFO_ABOUT_SHELTER);
                commandArrayList.add(UserCommand.HOW_TO_TAKE_ANIMAL);
                commandArrayList.add(UserCommand.SEND_REPORT);
                commandArrayList.add(UserCommand.CALL_VOLUNTEER);

                sendMessage(chatId,
                        "Keep it going! Choose, what you are  interested in: ",
                        createInlineKeyboard(commandArrayList));
            }
        }
    }

    private void switchCallback(CallbackQuery callbackQuery){
        if(callbackQuery.data().contains(UserCommand.CAT_SHELTER.toString())
                ||callbackQuery.data().contains(UserCommand.DOG_SHELTER.toString())){
            log.info("callback_data = animalShelter");

            ArrayList<UserCommand> commandArrayList = new ArrayList<>();
            commandArrayList.add(UserCommand.INFO_ABOUT_SHELTER);
            commandArrayList.add(UserCommand.HOW_TO_TAKE_ANIMAL);
            commandArrayList.add(UserCommand.SEND_REPORT);
            commandArrayList.add(UserCommand.CALL_VOLUNTEER);

            sendMessage(callbackQuery.message().chat().id(),
                    "Keep it going! Choose, what you are  interested in: ",
                    createInlineKeyboard(commandArrayList));

        } else if(callbackQuery.data().contains(UserCommand.INFO_ABOUT_SHELTER.toString())){
            log.info("callback_data = infoAboutShelter");

            ArrayList<UserCommand> commandArrayList = new ArrayList<>();
            commandArrayList.add(UserCommand.SHELTERS_ADDRESSES);
            commandArrayList.add(UserCommand.CAR_PASS);
            commandArrayList.add(UserCommand.SHELTER_RULES);
            commandArrayList.add(UserCommand.CONTACT_INFORMATION);
            commandArrayList.add(UserCommand.CALL_VOLUNTEER);

            sendMessage(callbackQuery.message().chat().id(),
                    "What do you want to know?",
                    createInlineKeyboard(commandArrayList));

        } else if(callbackQuery.data().contains(UserCommand.SHELTERS_ADDRESSES.toString())){
            log.info("callback_data = shelterAddress");

            ArrayList<City> cities = new ArrayList<>(List.of(City.values()));

            sendMessage(callbackQuery.message().chat().id(),
                    "Choose your city:",
                    createCityInlineKeyboard(cities));

        } else if(callbackQuery.data().contains("city_")){
            log.info("callback_data = City; " + callbackQuery.data());

            InlineKeyboardMarkup inlineKeyboard = chooseAnimalShelters(
                    City.valueOf(callbackQuery.data().substring(5)));
            sendMessage(callbackQuery.message().chat().id(),
                    "Choose your animal shelter:",
                    inlineKeyboard);

        }  else if(callbackQuery.data().contains("AS_")) {
            log.info("callback_data = " + callbackQuery.data());

            CurrChoice tempCurrChoice = new CurrChoice();
            tempCurrChoice.setCurrentDateTime(LocalDateTime.now());
            tempCurrChoice.setAppUser(appUserDAO.findByTelegramUserId(callbackQuery.from().id()));
            AnimalShelter tempAnimalShelter = animalShelterDAO.findById(
                    Long.valueOf(callbackQuery.data().substring(3))).orElse(null);
            if (tempAnimalShelter == null) {
                log.info("Warning! The non-existed animal shelter has chosen!");
            }
            tempCurrChoice.setAnimalShelter(tempAnimalShelter);
            CurrChoice savedCurrChoice = currChoiceDAO.save(tempCurrChoice);

            ArrayList<UserCommand> commandArrayList = new ArrayList<>();
            commandArrayList.add(UserCommand.SHELTER_WORKING_HOURS);
            commandArrayList.add(UserCommand.SHELTER_ADDRESS);
            commandArrayList.add(UserCommand.SHELTER_DRIVING_DIRECTION);

            sendMessage(callbackQuery.message().chat().id(),
                    "What would you like to know?",
                    createInlineKeyboard(commandArrayList));
        } else if(callbackQuery.data().contains(UserCommand.SHELTER_WORKING_HOURS.toString())){
            log.info("callback_data = " + callbackQuery.data());

            Long currAnimalShelterId =  currChoiceDAO.findAnimalShelterIdByChoiceId(
                    currChoiceDAO.findLastChoiceByAppUserId(
                            appUserDAO.findByTelegramUserId(callbackQuery.from().id()).getId()));
            String workingHours = animalShelterDAO.findWorkingHoursById(currAnimalShelterId);

            Map<String, String> buttons = new TreeMap<>();
            buttons.put(" " + workingHours,"AS_" + currAnimalShelterId);
            buttons.put("Return","AS_" + currAnimalShelterId);

            sendMessage(callbackQuery.message().chat().id(),
                    "We are open: ",
                    createFreeInlineKeyboard(buttons));
        }
    }

    private InlineKeyboardMarkup chooseAnimalShelters(City city){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<AnimalShelter> animalShelterList = animalShelterService.getAnimalSheltersByCity(city);
        for (int i=0; i<animalShelterList.size();i++){
            inlineKeyboard.addRow(new InlineKeyboardButton(animalShelterList.get(i).getName())
                    .callbackData("AS_" + animalShelterList.get(i).getId()));
        }
        return  inlineKeyboard;
    }

    //собрать метод, переработать алгоритм хождения по кнопкам, вызов callback
    private  InlineKeyboardMarkup createInlineKeyboard(ArrayList<UserCommand> commandArrayList){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        commandArrayList.forEach(e -> inlineKeyboard.addRow(new InlineKeyboardButton(e.getDescription())
                .callbackData(e.toString())));
        return inlineKeyboard;
    }

    private  InlineKeyboardMarkup createFreeInlineKeyboard(Map<String,String> buttons){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        for(Map.Entry<String,String> entry : buttons.entrySet()){
           inlineKeyboard.addRow(new InlineKeyboardButton(entry.getKey())
                    .callbackData(entry.getValue()));
        }
        return inlineKeyboard;
    }

    private  InlineKeyboardMarkup createCityInlineKeyboard(ArrayList<City> cities){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        cities.forEach(e -> inlineKeyboard.addRow(new InlineKeyboardButton(e.getName())
                .callbackData("city_" + e)));
        return inlineKeyboard;
    }

    private void sendMessage(Long chatId, String text, InlineKeyboardMarkup inlineKeyboard){
        SendMessage mess = new SendMessage(chatId,text)
                .replyMarkup(inlineKeyboard);
        telegramBot.execute(mess);
    }

}
