package models.ot.Enums;

/**
 * Created by amd on 11/25/15.
 */
public enum PermissionType {
    CAN_ADD_QUESTIONS(0),
    CAN_CREATE_TESTS(1),
    CAN_VIEW_RESULT(2),
    CAN_GIVE_TEST(3),
    CAN_MODIFY_SOLUTION(4),
    CAN_MODIFY_QUESTION(5),
    CAN_RESET_TEST(6),
    CAN_CREATE_RANDOM_TEST(7);


    public Long getEventValue() {
        return eventValue;
    }

    PermissionType(long eventType) {
        eventValue = eventType;
    }

    public final Long eventValue;

    }
