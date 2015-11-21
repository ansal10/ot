package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum QuestionType {
    MCQ(1),
    SUBJECTIVE(2);

    public Long getEventValue() {
        return eventValue;
    }

    QuestionType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
