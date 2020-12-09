package entity;

import constants.Keys;

public class SuccessAnswer extends Answer {

    @Override
    String getStatus() {
        return Keys.SUCCESS;
    }
}
