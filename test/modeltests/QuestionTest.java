package modeltests;

import models.ot.Option;
import models.ot.Question;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class QuestionTest {

    FakeApplication fakeApplication;

    String question1 = "what is answer of 1+2 ? ";
    String question2 = "what among these are true ? ";
    Question q1,q2,q3;
    List<String> options1 = Arrays.asList("1", "2", "3", "4");
    List<String> options2 = Arrays.asList("sun is hot", "moon has light", "earth is 3rd planet","our is milkyway galaxy");


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
    }

    @After
    public void tearDown() throws Exception {
        stop(fakeApplication);
    }


    @Test
    public void testAllOption(){
        List<Option> options = Option.find.all();

        assertEquals(8, options.size());
    }


    @Test
    public void testCorrectInsertion(){
        Question question = Question.findQuestion(question1);
        Question question_2 = Question.findQuestion(question2);
        assertNotNull(question);
        assertNotNull(question_2);
        assertEquals(question.getOptions().size() , options1.size());
        assertEquals(question.getCorrectOption().size(), 1);
        assertEquals(question_2.getCorrectOption().size(), 3);
    }


}