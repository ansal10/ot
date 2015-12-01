package models.ot.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum TestType {
    MATHS(0),
    AIEEE(1),
    SSC_CGL(2),
    IBS_PO(3);

    public Long getEventValue() {
        return eventValue;
    }

    TestType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;
}
