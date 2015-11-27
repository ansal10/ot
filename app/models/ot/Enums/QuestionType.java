package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum QuestionType {
    ENGLISH(1),
    MATHS(2),
    APTITUDE(3),
    REASONING(4),
    GENERAL_AWARENESS(5);

    public Long getEventValue() {
        return eventValue;
    }

    QuestionType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
