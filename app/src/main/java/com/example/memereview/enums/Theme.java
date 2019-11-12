package com.example.memereview.enums;

public enum Theme {

    RED, BLUE, BLACK;

    public static Theme determineTheme(String color) {
        Theme theme = null;
        switch(color) {
            case "red":
                theme = RED;
                break;
            case "blue":
                theme = BLUE;
                break;
            case "black":
                theme = BLACK;
                break;
        }
        return theme;
    }
}
