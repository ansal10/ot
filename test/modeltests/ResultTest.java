package modeltests;

import models.ot.Enums.DifficultyType;
import models.ot.Enums.QuestionType;
import models.ot.Enums.TestType;
import models.ot.Option;
import models.ot.Question;
import models.ot.Result;
import models.ot.Users;
import org.junit.After;
import org.junit.Before;
import play.test.FakeApplication;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class ResultTest {


    FakeApplication fakeApplication;

    String question1 = "what is answer of 1+2 ? ";
    String question2 = "what among these are true ? ";
    Question q1,q2,q3;
    List<String> options1 = Arrays.asList("1", "2", "3", "4");
    List<String> options2 = Arrays.asList("sun is hot", "moon has light", "earth is 3rd planet","our is milkyway galaxy");
    models.ot.Test test;
    Users u1,u2;

    @Before
    public void setUp() throws Exception {
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);

        q1 = new Question(question1, DifficultyType.AVERAGE, QuestionType.APTITUDE);
        q1.save();

        for(String o : options1){
            if(o.equals("3"))
                q1.addOption(o,true);
            else q1.addOption(o, false);
        }

        q2 = new Question(question2, DifficultyType.AVERAGE, QuestionType.APTITUDE);
        q2.save();

        for(String o:options2){
            if(o.equals("earth is 3rd planet")) {
                Option option = new Option(q2, o, false);
                option.save();
            }else {
                Option option = new Option(q2, o, true);
                option.save();
            }
        }
        test = new models.ot.Test("MCQ", TestType.AIEEE, (long)120,  (long)2, true, 10.0);
        test.save();

        u1 = new Users("anas", "pass", "a@gmail.com", "fname", "lname",true, false);
        u2 = new Users("anas2","pass","a@hotmail.com","fname", "lname", false, false);
        u1.save();
        u2.save();

        test.addQuestion(q1, 5.0, 1.0);
        test.addQuestion(q2, 5.0, 2.0);
    }

    @After
    public void tearDown() throws Exception {
        stop(fakeApplication);
    }

    @org.junit.Test
    public void testAssignTest(){
        Result result = new Result(u1, test);
        try {
            result.save();
            assertTrue(result.getId() != null);
        }finally {
            result.delete();
        }
    }

    @org.junit.Test
    public void testCorrectOptions(){
        assertEquals(q1.getCorrectOption().size(), 1);
        assertEquals(q2.getCorrectOption().size(), 3);
    }

    @org.junit.Test
    public void testResultSolvingWithOneCorrectAnswer(){
        Result result = new Result(u1, test);
        try {
            result.save();
            String response = "";
            response = response + test.getTestQuestionses().get(0).getQuestion().getId() + ":";
            List<Option> correctOptions = test.getTestQuestionses().get(0).getQuestion().getCorrectOption();
            for(int i = 0 ; i < correctOptions.size() ; i++){
                response = response+correctOptions.get(i).getId();
                if (i != (correctOptions.size()-1))
                    response=response+",";
            }
            result.setResponse(response);
            result.save();

            result.submitTest();

            assertTrue(result.getCorrectAnswer() == 1);
            assertEquals(result.getCorrectMarks(), 5.0, .001);
            assertTrue(result.getUnAttempted() == 1);
        }finally {
            result.delete();
        }
    }

    @org.junit.Test
    public void testResultSolvingWithOneCorrectOneWrondAnswer(){
        Result result = new Result(u1, test);
        try {
            result.save();
            String response = "";
            response = response + test.getTestQuestionses().get(0).getQuestion().getId() + ":";
            List<Option> correctOptions = test.getTestQuestionses().get(0).getQuestion().getCorrectOption();
            for(int i = 0 ; i < correctOptions.size() ; i++){
                response = response+correctOptions.get(i).getId();
                if (i != (correctOptions.size()-1))
                    response=response+",";
            }
            response = response+"\n"+q2.getId()+":3,5";
            result.setResponse(response);
            result.save();

            result.submitTest();

            assertTrue(result.getCorrectAnswer() == 1);
            assertTrue(result.getWrongAnswer() == 1);
            assertEquals(result.getCorrectMarks(), 5.0, .001);
            assertEquals(result.getWrongMarks(), 2.0, .001);
        }finally {
            result.delete();
        }
    }

}