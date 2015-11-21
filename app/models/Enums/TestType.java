package models.Enums;

/**
 * Created by amd on 11/20/15.
 */

public enum TestType {
    MATHS(1),
    AIEEE(2),
    SSC_CGL(3),
    IBS_PO(4);

    public long getEventValue() {
        return eventValue;
    }

    TestType(long eventType) {
        eventValue = eventType;
    }

    public final long eventValue;
}
