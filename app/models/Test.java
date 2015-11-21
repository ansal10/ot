package models;

import com.avaje.ebean.Model;
import models.Enums.TestType;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class Test  extends Model {

    @Id
    private Long id;
    private String name;
    private DateTime createdOn;
    private DateTime expiredOn;
    private TestType type;

    private Long totalQuestions;
    private Boolean isActive;
    private Long totalMarks;



    @ManyToMany
    private List<Users> users;

    @OneToMany
    private List<TestQuestions> testQuestionses;

}
