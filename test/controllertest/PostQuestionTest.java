package controllertest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.ot.mapper.Answer;
import controllers.ot.mapper.NewQuestionRequestMapper;
import models.ot.Enums.DifficultyType;
import models.ot.Enums.PermissionType;
import models.ot.Enums.QuestionType;
import models.ot.Option;
import models.ot.Question;
import models.ot.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.core.j.JavaResultExtractor;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.TestServer;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

/**
 * Created by amd on 11/27/15.
 */


public class PostQuestionTest {

    FakeApplication fakeApplication ;
    TestServer testServer;

    @Before
    public void init(){
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);
        Users users = new Users("anas","ansal10","anas.ansal10@gmail.com","anas","md",true,false);
        users.save();
        users.addPermission(PermissionType.CAN_ADD_QUESTIONS);
    }

    @After
    public void destroy(){
        stop(fakeApplication);
    }

    @Test
    public void testWithValidJSON() throws IOException {

        String rawJson = "{\"id\" : \"null\" ," +
                " \"question\" : \"This is an question\" , "+
                " \"answers\" : [{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"true\" }]," +
                "\"questionType\" : \"1\"," +
                "\"difficultyLevel\" : \"1\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(body.contains("success"));
        assertEquals(OK, result.status());
        assertEquals(Question.find.byId("1").getQuestion(), "This is an question");
    }


    @Test
    public void testWithDuplicateOption() throws IOException {
        String rawJson = "{ \"id\" : \"null\" ," +
                "\"question\" : \"This is an question\" , "+
                " \"answers\" : [{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"true\" }," +
                "{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"true\" }]," +
                "\"questionType\" : \"1\"," +
                "\"difficultyLevel\" : \"1\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(body.contains("error"));
        assertTrue(body.contains(NewQuestionRequestMapper.DUPLICATE_OPTION));
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testWithNoCorrectOption() throws IOException{
        String rawJson = "{\"id\" : \"null\" ," +
                " \"question\" : \"This is an question\" , "+
                " \"answers\" : [{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"false\" }," +
                "{\"index\":\"2\" , \"value\":\"option 2\", \"correct\":\"false\" }]," +
                "\"questionType\" : \"1\"," +
                "\"difficultyLevel\" : \"1\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(body.contains("error"));
        assertTrue(body.contains(NewQuestionRequestMapper.CORRECT_OPTION_MISSING));
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testWithNoOption() throws IOException{
        String rawJson = "{ \"id\" : \"null\" ," +
                "\"question\" : \"This is an question\" , "+
                " \"answers\" : []," +
                "\"questionType\" : \"1\"," +
                "\"difficultyLevel\" : \"1\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(!body.contains("success"));
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testWithInvalidEnums() throws IOException{
        String rawJson = "{ \"id\" : \"null\" ," +
                "\"question\" : \"This is an question\" , "+
                " \"answers\" : [{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"true\" }]," +
                "\"questionType\" : \"10\"," +
                "\"difficultyLevel\" : \"10\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(!body.contains("success"));
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testEditQuestion() throws IOException {

        Question question = new Question("This is test question", DifficultyType.AVERAGE, QuestionType.APTITUDE);
        question.save();
        question.addOption(new Option(question, "Option1", true));
        question.addOption(new Option(question, "Option2", false));

        assertEquals(Option.find.all().size(), 2);
        String rawJson = "{\"id\" : \"1\" ," +
                " \"question\" : \"This is an question\" , "+
                " \"answers\" : [{\"index\":\"1\" , \"value\":\"option 1\", \"correct\":\"true\" }]," +
                "\"questionType\" : \"1\"," +
                "\"difficultyLevel\" : \"1\"," +
                "\"key\" : \"mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV\" }";

        JsonNode jsonNode = new ObjectMapper().readTree(rawJson);
        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/new_question").bodyJson(jsonNode);
        fakeRequest.session("username", "anas");
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        assertTrue(body.contains("success"));
        assertEquals(OK, result.status());
        assertEquals(Question.find.byId("1").getQuestion(), "This is an question");
        assertEquals(Question.find.all().size(),1);
        Question dbQuestion = Question.find.byId("1");
        assertEquals(dbQuestion.getQuestion(), "This is an question");
        assertEquals(dbQuestion.getOptions().get(0).getOption(), "option 1");
        assertEquals(Option.find.all().size(),1);

    }

    @Test
    public void testAnswerHashSet(){
        Answer a1 = new Answer();
        a1.setCorrect(true);
        a1.setIndex(1);
        a1.setValue("Val1");

        Answer a2  = new Answer();
        a2.setCorrect(true);
        a2.setIndex(1);
        a2.setValue("Val1");

        HashSet<Answer> hashSet = new HashSet<>();
        hashSet.add(a1);
        hashSet.add(a2);
        assertTrue(hashSet.size() != 2);
    }
}
