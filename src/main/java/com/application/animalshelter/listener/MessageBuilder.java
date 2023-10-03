package com.application.animalshelter.listener;

import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.dao.ShelterDAO;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.enums.AnimalType;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import static com.application.animalshelter.enums.Commands.*;

/**
 * Класс MessageBuilder предоставляет методы для создания сообщений (SendMessage) в ответ на команды пользователя в телеграм-боте.
 * Он используется для формирования текстовых сообщений с различными вариантами ответов и клавиатурами.
 */
@Service
public class MessageBuilder {
    private final AppUserDAO appUserDAO;
    private final ShelterDAO shelterDAO;

    public MessageBuilder(AppUserDAO appUserDAO, ShelterDAO shelterDAO) {
        this.appUserDAO = appUserDAO;
        this.shelterDAO = shelterDAO;
    }

    /**
     * Создает приветственное сообщение для пользователя с указанным идентификатором чата и именем пользователя.
     *
     * @param chatID   Идентификатор чата с пользователем.
     * @param userName Имя пользователя.
     * @return SendMessage с приветственным сообщением и клавиатурой выбора приюта.
     */
    public SendMessage getStartMessage(long chatID, String userName) {
        return new SendMessage(chatID, "Добро пожаловать, " + userName + "!" + '\n' + '\n' +
                getBotInfoMessage() + '\n' + '\n' +
                "Пожалуйста, выбери приют:").replyMarkup(getShelterTypeKeyboard());
    }

    /**
     * Создает сообщение в зависимости от кнопки, нажатой пользователем.
     *
     * @param chatId          Идентификатор чата с пользователем.
     * @param userId          Идентификатор пользователя.
     * @param callbackDataText Текст сообщения, полученный от пользователя.
     * @return SendMessage с соответствующим ответом на текст пользователя и клавиатурой.
     */
    public SendMessage getReplyMessage(long chatId, String callbackDataText, long userId) {
        return switch (callbackDataText) {
            case "Информация о боте" -> new SendMessage(chatId, getBotInfoMessage());
            case "Вернуться к выбору приюта" -> new SendMessage(chatId, "Пожалуйста, выберите приют:").replyMarkup(getShelterTypeKeyboard());
            case "Приют для кошек", "Приют для собак" -> new SendMessage(chatId, "Выбери, что тебя интересует:").replyMarkup(getStartMenuKeyboard());
            case "Узнать информацию о приюте" -> new SendMessage(chatId, "Выбери, что тебя интересует:").replyMarkup(getShelterMenuKeyboard());
            case "Адреса приютов и время работы" -> new SendMessage(chatId, getInfoShelter(userId));
            case "Оформление пропуска на машину" -> new SendMessage(chatId, getPassRulesShelter(userId));
            case "Правила и ТБ" -> new SendMessage(chatId, getShelterRules(userId));
            case "Как взять животное из приюта" -> new SendMessage(chatId,"Отлично, что далее:").replyMarkup(getAnimalReceiptKeyboard(userId));
            case "Записать контактные данные" -> new SendMessage(chatId, "Пожалуйста, отправьте свой номер телефона и почту текстовым сообщением. прим: 81234567890 gmail@gmail.com");
            default -> new SendMessage(chatId, "Я не понимаю вашу команду. Выберите команду из списка:").replyMarkup(getStartMenuKeyboard());
        };
    }

    /**
     * Создает сообщение с информацией о боте.
     *
     * @return String с информацией о боте.
     */
    public String getBotInfoMessage() {
        return "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информацию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке";
    }

    /**
     * Возвращает приют в зависимости от выбранного типа животных у пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект Shelter, представляющий приют.
     */
    private Shelter getShelterByType(long userId) {
        String shelterType = appUserDAO.findByTelegramUserId(userId).getShelterType();
        if (shelterType.equals(SHELTER_CAT.getText())) {
            return shelterDAO.findByAnimalType(AnimalType.CAT);
        } else return shelterDAO.findByAnimalType(AnimalType.DOG);
    }

    /**
     * Возвращает информацию о приюте, включая его название, адрес и режим работы.
     *
     * @param userId Идентификатор пользователя.
     * @return Информация о приюте в виде строки.
     */
    private String getInfoShelter(long userId) {
        Shelter shelter = getShelterByType(userId);
        return shelter.getName() + '\n' + "Адрес: " + shelter.getAddress() + '\n' + "Режим работы :" + shelter.getWorkingHours();
    }

    /**
     * Возвращает требования для получения пропуска в приюте.
     *
     * @param userId Идентификатор пользователя.
     * @return Строка, содержащая правила и требования для получения пропуска.
     */
    private String getPassRulesShelter(long userId) {
        Shelter shelter = getShelterByType(userId);
        return "Данные охраны для получения пропуска: " + shelter.getPassRules();
    }

    /**
     * Возвращает правила пребывания и взаимодействия с животными в приюте.
     *
     * @param userId Идентификатор пользователя.
     * @return Строка, содержащая правила пребывания и общения с животными в приюте.
     */
    private String getShelterRules(long userId) {
        Shelter shelter = getShelterByType(userId);
        return shelter.getShelterRules();
    }

    /**
     * Создает клавиатуру для выбора типа приюта (для кошек или собак).
     *
     * @return InlineKeyboardMarkup с кнопками выбора типа приюта.
     */
    public Keyboard getShelterTypeKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(SHELTER_CAT.getText() + "\uD83D\uDC08")
                        .callbackData(SHELTER_CAT.getText()),
                new InlineKeyboardButton(SHELTER_DOG.getText() + "\uD83D\uDC15")
                        .callbackData(SHELTER_DOG.getText()));
    }

    /**
     * Создает клавиатуру для главного меню.
     *
     * @return InlineKeyboardMarkup с кнопками команд главного меню.
     */
    public Keyboard getStartMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(INFO_SHELTER.getText())
                .callbackData(INFO_SHELTER.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ADOPT_PET.getText())
                .callbackData(ADOPT_PET.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(SEND_REPORT.getText())
                .callbackData(SEND_REPORT.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getText())
                .callbackData(BACK_TO_SHELTER_SELECTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BOT_INFORMATION.getText())
                .callbackData(BOT_INFORMATION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getText())
                .callbackData(CALL_VOLUNTEER.getText()));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру для меню приюта.
     *
     * @return InlineKeyboardMarkup с кнопками команд.
     */
    public Keyboard getShelterMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ADDRESS_AND_WORKING_HOURS.getText())
                .callbackData(ADDRESS_AND_WORKING_HOURS.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CAR_PASS.getText())
                .callbackData(CAR_PASS.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(RULES_AND_INTERACTION.getText())
                .callbackData(RULES_AND_INTERACTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CONTACT_DETAILS.getText())
                .callbackData(CONTACT_DETAILS.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_MENU.getText())
                .callbackData(BACK_TO_SHELTER_MENU.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getText())
                .callbackData(BACK_TO_SHELTER_SELECTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getText())
                .callbackData(CALL_VOLUNTEER.getText()));
        return inlineKeyboardMarkup;
    }


    /**
     * Метод для создания клавиатуры с вариантами для получения информации о животных из приюта.
     *
     * @param userId Идентификатор пользователя, для определения типа приюта (кошки или собаки).
     * @return Возвращает InlineKeyboardMarkup с кнопками команд.
     */
    private Keyboard getAnimalReceiptKeyboard(long userId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ANIMAL_INTRODUCTION.getText())
                .callbackData(ANIMAL_INTRODUCTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(DOCUMENTS_REQUIRED.getText())
                .callbackData(DOCUMENTS_REQUIRED.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(TRANSPORTATION.getText())
                .callbackData(TRANSPORTATION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(PUPPY_KITTEN.getText())
                .callbackData(PUPPY_KITTEN.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ADULT_ANIMAL.getText())
                .callbackData(ADULT_ANIMAL.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(DISABLED_ANIMAL.getText())
                .callbackData(DISABLED_ANIMAL.getText()));
        if (appUserDAO.findByTelegramUserId(userId).getShelterType().equals(SHELTER_DOG.getText())) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(TRAINING_TIPS.getText())
                    .callbackData(TRAINING_TIPS.getText()));
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(DOG_TRAINERS.getText())
                    .callbackData(DOG_TRAINERS.getText()));
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(REASONS_FOR_DENIAL.getText())
                    .callbackData(REASONS_FOR_DENIAL.getText()));
        }
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CONTACT_DETAILS.getText())
                .callbackData(CONTACT_DETAILS.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_MENU.getText())
                .callbackData(BACK_TO_SHELTER_MENU.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getText())
                .callbackData(BACK_TO_SHELTER_SELECTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getText())
                .callbackData(CALL_VOLUNTEER.getText()));
        return inlineKeyboardMarkup;
    }

}