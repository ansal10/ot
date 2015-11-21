package models;

import com.avaje.ebean.Model;
import models.Enums.TestType;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
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
    private Long durations;
    private Long totalQuestions;
    private Boolean isActive;
    private Double totalMarks;



    @OneToMany
    private List<Result> results;

    @OneToMany(mappedBy = "test")
    private List<TestQuestions> testQuestionses;

    public static Finder<String, Test> find = new Finder<String, Test>(Test.class);

    public Test(String name, TestType type, Long totalQuestions, Boolean isActive, Double totalMarks) {
        DateTime now = new DateTime();
        this.name = name;
        this.type = type;
        this.totalQuestions = totalQuestions;
        this.isActive = isActive;
        this.totalMarks = totalMarks;
        this.createdOn = now;
        this.expiredOn = now.plusYears(1);
    }

    public Test(String name, TestType type, Long durations, Long totalQuestions, Boolean isActive, Double totalMarks) {
        DateTime now = new DateTime();
        this.name = name;
        this.type = type;
        this.durations = durations;
        this.totalQuestions = totalQuestions;
        this.isActive = isActive;
        this.totalMarks = totalMarks;
        this.createdOn = now;
        this.expiredOn = now.plusYears(1);
    }

    public void addQuestion(Question question, Double correctMarks, Double wrongMarks) throws Exception {

        List<TestQuestions> existingQuestions = this.getTestQuestionses();
        if(existingQuestions.size()==this.totalQuestions)
            throw new Exception(ALREADY_DESIRED_NO_OF_QUESTION_EXIST);

        Double totalMarksForExistingQuestions = 0.0;
        for(TestQuestions testQuestion : existingQuestions)
            totalMarksForExistingQuestions+=testQuestion.getCorrectAnswerMark();

        if(totalMarksForExistingQuestions + correctMarks > this.totalMarks)
            throw new Exception(ADDING_QUESTION_EXCEEDS_TOTAL_MARKS);



        question = Question.findQuestion(question.getQuestion());
        if(question==null)
            throw new Exception(QUESTION_DOES_NOT_EXIST);

        TestQuestions testQuestion = new TestQuestions(this, question, correctMarks, wrongMarks);
        this.testQuestionses.add(testQuestion);
        testQuestion.save();
    }

    public void save(){
        super.save();
        super.refresh();
    }





    public static final String QUESTION_DOES_NOT_EXIST = "Question does not exist in database";
    public static final String ALREADY_DESIRED_NO_OF_QUESTION_EXIST = "Already desired number of question exist for the test";
    public static final String ADDING_QUESTION_EXCEEDS_TOTAL_MARKS = "Adding this question exceeds the total marks assigned for test";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public DateTime getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(DateTime expiredOn) {
        this.expiredOn = expiredOn;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<TestQuestions> getTestQuestionses() {
        return testQuestionses;
    }

    public void setTestQuestionses(List<TestQuestions> testQuestionses) {
        this.testQuestionses = testQuestionses;
    }

    public Long getDurations() {
        return durations;
    }

    public void setDurations(Long durations) {
        this.durations = durations;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
