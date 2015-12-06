package controllers.ot.mapper;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.OtApplication;
import jdk.nashorn.internal.ir.annotations.Ignore;
import models.ot.Enums.DifficultyType;
import models.ot.Enums.QuestionType;
import models.ot.Question;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by amd on 11/27/15.
 */
public class NewQuestionRequestMapper {

    public static final String QUESTION_MISSING = "Question Missing";
    public static final String QUESTION_TYPE_MISSING = "Question Type is Missing";
    public static final String DIFFICULTY_LEVEL_MISSING = "Difficulty Level is Missing";
    public static final String API_KEY_ERROR = "API Key Error";
    public static final String OPTION_INVALID = "Option number %s is Invalid";
    public static final String CORRECT_OPTION_MISSING = "No Option Marked as Correct";
    public static final String DUPLICATE_OPTION = "Duplicate Options are not Allowed";

    private Long id;
    private String question;
    private List<Answer> answers;
    private QuestionType questionType;
    private DifficultyType difficultyLevel;
    private String key;

    @Ignore
    private List<String> errors;

    @Ignore
    private Long questionId;



    public  NewQuestionRequestMapper parseFromJSON(JsonNode json){
        NewQuestionRequestMapper mapper;
        try {
            mapper = Json.fromJson(json, NewQuestionRequestMapper.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        mapper.errors = new ArrayList<>();
        if(mapper.question == null || mapper.question.trim().equals("")){
            mapper.errors.add(QUESTION_MISSING);
        }
        if(mapper.questionType == null)
            mapper.errors.add(QUESTION_TYPE_MISSING);

        if(mapper.difficultyLevel == null)
            mapper.errors.add(DIFFICULTY_LEVEL_MISSING);

        if(!mapper.key.equals(OtApplication.NEW_QUESTION_KEY))
            mapper.errors.add(API_KEY_ERROR);

        int correctAnswer=0;
        for(Answer answer : mapper.answers){
            if(!answer.isValid())
                mapper.errors.add(String.format(OPTION_INVALID, answer.getIndex()+""));
            else {
                if(answer.isCorrect())
                    correctAnswer++;
            }
        }

        if(correctAnswer==0)
            mapper.errors.add(CORRECT_OPTION_MISSING);

        HashSet<Answer> uniqueAnswer = new HashSet<Answer>();
        uniqueAnswer.addAll(mapper.answers);

        if(mapper.answers.size() > uniqueAnswer.size())
            mapper.errors.add(DUPLICATE_OPTION);

        return mapper;

    }

    public boolean saveToDB(){
        Ebean.beginTransaction();
        Question newQuestion = new Question(this.question, this.difficultyLevel, this.questionType);
        try{
            if(this.id!=null){
               Question.find.byId(String.valueOf(this.id)).delete();
               newQuestion.setId(this.id);
            }
            newQuestion.save();
            for (Answer answer: answers){
                newQuestion.addOption(answer.getValue(), answer.getCorrect());
            }
            Ebean.commitTransaction();
            this.questionId = newQuestion.getId();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Ebean.rollbackTransaction();
            this.questionId = null;
            return false;
        }finally {
            Ebean.endTransaction();
        }
    }



    public boolean hasErrors(){
        return errors.size() > 0;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public DifficultyType getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyType difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
