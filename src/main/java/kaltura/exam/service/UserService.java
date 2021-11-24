package kaltura.exam.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kaltura.exam.dto.CreateExistingUserResponse;
import kaltura.exam.dto.CreateUserRequest;
import kaltura.exam.dto.CreateUserResponse;
import kaltura.exam.dto.LoginUserResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;

public class UserService {
    private String uriCreateUser = "https://api.frs1.ott.kaltura.com/api_v3/service/ottuser/action/register";
    private String uriLoginUser = "https://api.frs1.ott.kaltura.com/api_v3/service/ottuser/action/login";

    public CreateUserResponse createUser(String jsonCreateUser) throws URISyntaxException, IOException, InterruptedException {
        return getResponse(CreateUserResponse.class,uriCreateUser,jsonCreateUser);
    }

    public LoginUserResponse loginUser(String jsonLoginUser) throws URISyntaxException, IOException, InterruptedException {
        return getResponse(LoginUserResponse.class,uriLoginUser,jsonLoginUser);
    }

    public CreateExistingUserResponse createExistingUser(String jsonCreateUser) throws URISyntaxException, IOException, InterruptedException {
        return getResponse(CreateExistingUserResponse.class,uriCreateUser,jsonCreateUser);
    }

    private <T>T getResponse(Class<T> responseClass, String uri, String json) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = getRequest(uri, json);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //for(header of response.headers())
        System.out.printf("Response headers: %s%n",response.headers());
        System.out.printf("Response body: %s%n",response.body());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.body(), responseClass);
    }

    private HttpRequest getRequest(String uri, String json) throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        System.out.printf("Request headers: %s%n",request.headers());;

        HttpRequest.BodyPublisher bodyPublisher = request.bodyPublisher().get();
        FlowSubscriber<ByteBuffer> flowSubscriber = new FlowSubscriber<>();
        bodyPublisher.subscribe(flowSubscriber);
        String requestBody = new String(flowSubscriber.getBodyItems().get(0).array());
        System.out.printf("Request body: %s%n",requestBody);
        return request;
    }


    public String getJsonLoginUser(String jsonCreateUser) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CreateUserRequest createUserRequest = mapper.readValue(jsonCreateUser, CreateUserRequest.class);

        return "{\n" +
                "  \"apiVersion\": \"5.3.0\",\n" +
                "  \"partnerId\": 3197,\n" +
                "  \"username\": \""+createUserRequest.user.username+"\",\n" +
                "  \"password\": \""+createUserRequest.password+"\",\n" +
                "  \"extraParams\": {}\n" +
                "}\n";

    }

}
