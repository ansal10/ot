package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum QuestionType {
    ENGLISH(0),
    MATHS(1),
    APTITUDE(2),
    REASONING(3),
    GENERAL_AWARENESS(4);

    public Long getEventValue() {
        return eventValue;
    }

    QuestionType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
