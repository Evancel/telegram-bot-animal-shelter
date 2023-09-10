package com.application.animalshelter.listener;


public enum Commands {
    SHELTER_DOG("Приют для собак"),
    SHELTER_CAT("Приют для кошек"),
    INFO_SHELTER("Узнать информацию о приюте"),
    ADOPT_PET("Как взять животное из приюта"),
    SEND_REPORT("Прислать отчет о питомце"),
    CALL_VOLUNTEER("Позвать волонтера"),
    BACK_TO_SHELTER_SELECTION("Вернуться к выбору приюта"),
    BOT_INFORMATION("Информация о боте");

    Commands(String command) {
        this.command = command;
    }
    private final String command;

    public String getCommand() {
        return command;
    }
}
