package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 7. 31.
 * EventBus Message.
 * Palette Fragment -> Activity
 */

public class PaletteMessage {
    private int type;       // 보정 종류
    private int value;      // 보정 값

    public PaletteMessage(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
