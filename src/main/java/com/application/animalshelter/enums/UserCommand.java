package com.application.animalshelter.enums;

public enum UserCommand {
    START("/start","Press to start"),
    HELLO("/hello","Say hello"),
    CAT_SHELTER("catShelter","Cat shelter " + "\uD83D\uDC08"),
    DOG_SHELTER("dogShelter","Dog shelter " + "\uD83D\uDC15"),
    INFO_ABOUT_SHELTER("infoAboutShelter","Information about shelters"),
    HOW_TO_TAKE_ANIMAL("howToTakeAnimal","How to adopt an animal from a shelter"),
    SEND_REPORT("sendReport","To send a report about your animal"),
    CALL_VOLUNTEER("callVolunteer","To call a volunteer"),
    SHELTERS_ADDRESSES("shelterAddresses","Shelters addresses and opening hours"),
    CAR_PASS("carPass","Applying for a car pass"),
    SHELTER_RULES("shelterRules","Rules for staying inside and communicating with animals"),
    CONTACT_INFORMATION("contactInformation","Our contact information"),
    SHELTER_WORKING_HOURS("shelterWorkingHours","Our working hours"),
    SHELTER_ADDRESS("shelterAddress","Shelter address"),
    SHELTER_DRIVING_DIRECTION("shelterDrivingDirection","Driving directions");


    private final String value;
    private final String description;
    UserCommand (String value, String desciption){
        this.value=value;
        this.description=desciption;
    }

    public String getDescription(){
        return this.description;
    }
}
