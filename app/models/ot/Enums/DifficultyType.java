package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum DifficultyType {
    VERY_EASY(1),
    EASY(2),
    AVERAGE(3),
    HARD(4),
    VERY_HARD(5),
    EXTREME_HARD(6);


    public Long getEventValue() {
        return eventValue;
    }

    DifficultyType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
