package ua.svasilina.spedition.constants;

import static ua.svasilina.spedition.constants.Keys.SPACE;

public interface Patterns {
    String DATE_PATTERN = "dd.MM.yy";
    String TIME_PATTERN = "HH:mm";
    String DATE_TIME_PATTERN = TIME_PATTERN + SPACE + DATE_PATTERN;
}
