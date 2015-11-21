package modeltests;

import models.Enums.TestType;
import models.Option;
import models.Question;
import models.TestQuestions;
import models.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class TestTest {
    FakeApplication fakeApplication;

    String question1 = "what is answer of 1+2 ? ";
    String question2 = "what among these are true ? ";
    Question q1,q2;
    List<String> options1 = Arrays.asList("1", "2", "3", "4");
    List<String> options2 = Arrays.asList("sun is hot", "moon has light", "earth is 3rd planet","our is milkyway galaxy");
    Users u1,u2;
    models.Test test;

    @Before
    public void setUp() throws Exception {
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);
        q1 = new Question(question1);
        q1.save();

        for(String o : options1){
            if(o.equals("3"))
                q1.addOption(o,true);
            else q1.addOption(o, false);
        }

        q2 = new Question(question2);
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
        u1 = new Users("anas", "pass", "a@gmail.com", true, false);
        u2 = new Users("anas2","pass","a@hotmail.com", false, false);
        u1.save();
        u2.save();

        test = new models.Test("MCQ", TestType.AIEEE, (long)2, true, 10.0);
        test.save();
    }

    @After
    public void tearDown() throws Exception {
        stop(fakeApplication);
    }

    @Test
    public void createTest(){
        assertTrue(test.getId() != null);
    }

    @Test
    public void addQuestions() throws Exception {
        test.addQuestion(q1, 5.0, 1.0);
        test.addQuestion(q2, 5.0, 2.0);
        assertEquals(test.getTestQuestionses().size(), 2);
        assertEquals(TestQuestions.find.all().size(), 2);

    }
}