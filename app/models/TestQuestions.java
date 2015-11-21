package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class TestQuestions extends Model {


    @Id
    private Long id;

    @ManyToOne
    private Test test;

    @ManyToOne
    private Question question;


    private Double correctAnswerMark;
    private Double wrongAnswerMark;

    public static Finder<String, TestQuestions> find = new Finder<String, TestQuestions>(TestQuestions.class);

    public TestQuestions(Test test, Question question, Double correctMarks, Double wrongMarks){
        this.test = test;
        this.question = question;
        this.correctAnswerMark = correctMarks;
        this.wrongAnswerMark = wrongMarks;
    }

    public void save(){
        super.save();
        super.refresh();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Double getCorrectAnswerMark() {
        return correctAnswerMark;
    }

    public void setCorrectAnswerMark(Double correctAnswerMark) {
        this.correctAnswerMark = correctAnswerMark;
    }

    public Double getWrongAnswerMark() {
        return wrongAnswerMark;
    }

    public void setWrongAnswerMark(Double wrongAnswerMark) {
        this.wrongAnswerMark = wrongAnswerMark;
    }
}
