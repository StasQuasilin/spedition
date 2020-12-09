package entity;

import constants.Keys;

public class ErrorAnswer extends Answer {

    public ErrorAnswer(String message) {
        addParam(Keys.MESSAGE, message);
    }

    @Override
    String getStatus() {
        return Keys.ERROR;
    }
}
