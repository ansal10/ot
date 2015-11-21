package models.ot;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.*;

/**
 * Created by amd on 11/21/15.
 */

@Entity
public class Result extends Model {


    @Id
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Test test;

    @Column(length = 102400)
    private String response;

    private DateTime startedOn;
    private DateTime submittedOn;
    private DateTime endsOn;

    private Long correctAnswer;
    private Long wrongAnswer;
    private Long unAttempted;
    private Double correctMarks;
    private Double wrongMarks;

    public Result(Users user, Test test){
        DateTime now = new DateTime();
        this.user = user;
        this.test = test;
        this.startedOn=now;
        this.endsOn = now.plusSeconds(test.getDurations().intValue());

        correctAnswer = wrongAnswer = unAttempted = (long)0;
        correctMarks = wrongMarks = 0.0;


    }

    public boolean isExpired(){
        DateTime now = new DateTime();
        return now.getMillis() > this.endsOn.getMillis();
    }

    public void save(){
        super.save();
        super.refresh();
    }

    public void submitTest(){
        parseResult();
        calculateResult();
        super.save();
        super.refresh();
    }

    private void calculateResult() {

        List<TestQuestions> testQuestions = this.test.getTestQuestionses();

        for(TestQuestions testQuestion:testQuestions){
            List<Option> options = testQuestion.getQuestion().getCorrectOption();
            List<Long> correctOptionIds = new ArrayList<>();
            for(Option option:options)
                correctOptionIds.add(option.getId());

            Long questionId = testQuestion.getQuestion().getId();

            if(responseMap.containsKey(questionId)){
                List<Long> responseIds = responseMap.get(questionId);
                if (correct(responseIds, correctOptionIds)){
                    correctAnswer++;
                    correctMarks = correctMarks+testQuestion.getCorrectAnswerMark();
                }else {
                    wrongAnswer++;
                    wrongMarks = wrongMarks+testQuestion.getWrongAnswerMark();
                }
            }else {
                unAttempted++;
            }
        }

    }

    public void parseResult(){
        if( responseMap.isEmpty()){
           String []results = response.split("\n");
           for(String result : results){
               String []res = result.split(":");
               Long questionId = Long.parseLong(res[0].trim());
               String []options = res[1].split(",");
               List<Long> optionIds = new ArrayList<>();
               for(String s:options)
                   optionIds.add(Long.parseLong(s.trim()));

               responseMap.put(questionId, optionIds);
           }
        }
    }

    private boolean correct(List<Long> list1, List<Long> list2){
        if(list1.size() != list2.size() )
            return false;
        Collections.sort(list1);
        Collections.sort(list2);

        for(int i = 0 ; i < list1.size() ; i++){
            if(!list1.get(i).equals(list2.get(i)))
                return false;
        }
        return true;
    }








    private final Map<Long,List<Long>> responseMap = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public DateTime getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(DateTime startedOn) {
        this.startedOn = startedOn;
    }

    public DateTime getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(DateTime endsOn) {
        this.endsOn = endsOn;
    }

    public DateTime getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(DateTime submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Long getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Long correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(Long wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public Long getUnAttempted() {
        return unAttempted;
    }

    public void setUnAttempted(Long unAttempted) {
        this.unAttempted = unAttempted;
    }

    public Double getCorrectMarks() {
        return correctMarks;
    }

    public void setCorrectMarks(Double correctMarks) {
        this.correctMarks = correctMarks;
    }

    public Double getWrongMarks() {
        return wrongMarks;
    }

    public void setWrongMarks(Double wrongMarks) {
        this.wrongMarks = wrongMarks;
    }
}
