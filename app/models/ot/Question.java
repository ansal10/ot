package models.ot;

import com.avaje.ebean.Model;
import models.ot.Enums.DifficultyType;
import models.ot.Enums.QuestionType;

import javax.persistence.*;
import java.util.ArrayList;
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

    private DifficultyType difficultyType;

    private QuestionType questionType;


    @OneToMany
    private List<TestQuestions> testQuestionses;

    public static Finder<String, Question> find = new Finder<String, Question>(Question.class);


    public Question(String question, DifficultyType difficultyType, QuestionType questionType) {
        this.question = question;
        this.difficultyType = difficultyType;
        this.questionType = questionType;
    }

    public void addOption(String option, Boolean isCorrect){
        Option o = new Option(this, option, isCorrect);
        o.save();
    }

    public void addOption(Option option){
        option.setQuestion(this);
        option.save();
    }

    public static Question findQuestion(String question){
        Question question1 = Question.find.where().eq("question", question).findUnique();
        return question1;
    }

    public void save(){
        super.save();
        super.refresh();
    }

    public void delete(){
        for(Option option:this.getOptions()){
            option.delete();
        }
        super.delete();
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<TestQuestions> getTestQuestionses() {
        return testQuestionses;
    }

    public void setTestQuestionses(List<TestQuestions> testQuestionses) {
        this.testQuestionses = testQuestionses;
    }

    public List<Option> getCorrectOption(){
        List<Option> options = new ArrayList<>();
        for(Option o:this.getOptions()){
            if(o.isCorrect())
                options.add(o);
        }
        return options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DifficultyType getDifficultyType() {
        return difficultyType;
    }

    public void setDifficultyType(DifficultyType difficultyType) {
        this.difficultyType = difficultyType;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
}
