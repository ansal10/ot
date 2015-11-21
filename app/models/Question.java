package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class Question  extends Model {

    @Id
    private Long id;

    @Column(length = 10240, unique = true)
    private String question;

    @OneToMany
    private List<Option> options;

    private Long correctOption;
    private String correctAnswer;

    @OneToMany
    private List<TestQuestions> testQuestionses;



}
