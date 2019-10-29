package com.example.memereview.enums;

public enum Theme {

    RED, BLUE, BLACK;

    public static Theme determineTheme(String color) {
        Theme theme = null;
        switch(color) {
            case "red":
                theme = RED;
            case "blue":
                theme = BLUE;
            case "black":
                theme = BLACK;
        }
        return theme;
    }
}
