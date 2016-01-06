package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum DifficultyType {
    VERY_EASY(0),
    EASY(1),
    AVERAGE(2),
    HARD(3),
    VERY_HARD(4),
    EXTREME_HARD(5);


    public Long getEventValue() {
        return eventValue;
    }

    DifficultyType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
