package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class TestQuestions extends Model {


    @ManyToOne
    private Test test;

    @ManyToOne
    private Question question;


    private Long correctAnswerMark;
    private Long wrongAnswerMark;


}
