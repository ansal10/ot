package models.ot;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/21/15.
 */

@Entity
public class Option extends Model {


    @Id
    private Long id;

    @ManyToOne
    private Question question;

    @Column(length = 10240)
    private String option;

    private Boolean correct;


    public Option(Question question, String option, Boolean correct) {
        this.question = question;
        this.option = option;
        this.correct = correct;
    }


    public static Finder<String, Option> find = new Finder<String, Option>(Option.class);


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect(){
        return this.correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
