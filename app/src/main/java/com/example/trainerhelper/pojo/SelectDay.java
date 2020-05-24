package com.example.trainerhelper.pojo;

import android.content.res.Resources;
import android.graphics.Color;

import com.example.trainerhelper.R;

public class SelectDay {
    private String dayOfWeek;
    private int background;
    private boolean chosen;
    private int fontColor;


    public SelectDay(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.background = Color.WHITE;
        this.chosen = false;
        this.fontColor = Color.GRAY;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getBackground() {
        return background;
    }

    public boolean isChosen() {
        return chosen;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }
}
