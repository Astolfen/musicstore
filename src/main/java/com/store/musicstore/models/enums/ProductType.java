package com.store.musicstore.models.enums;

public enum ProductType {
    GUITAR("Гитара"),
    DRUM("Барабаны"),
    KEYBOARD("Клавишные"),
    MICROPHONE("Микрофон"),
    SPEAKER("Колонки");

    private final String russianName;

    ProductType(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
