package kaltura.exam;

import kaltura.exam.dto.CreateExistingUserResponse;
import kaltura.exam.dto.CreateUserResponse;
import kaltura.exam.dto.LoginUserResponse;
import kaltura.exam.service.UserService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;


public class TestApi {
    private UserService userService = new UserService();

    private String jsonCreateUser =
            "{\n" +
                    "  \"apiVersion\": \"5.3.0\",\n" +
                    "  \"partnerId\": 3197,\n" +
                    "  \"user\": {\n" +
                    "    \"objectType\": \"KalturaOTTUser\",\n" +
                    "    \"username\": \"QATest_158513041731326\",\n" +
                    "    \"firstName\": \"ott_user_lWkiwzTJJGYI6\",\n" +
                    "    \"lastName\": \"15851304173300\",\n" +
                    "    \"email\": \"QATest_15851304173130@mailinator.com\",\n" +
                    "    \"address\": \"ott_user_lWkiwzTJJGYI fake address\",\n" +
                    "    \"city\": \"ott_user_lWkiwzTJJGYI fake city\",\n" +
                    "    \"countryId\": 5,\n" +
                    "    \"externalId\": \"31640041226\"\n" +
                    "  },\n" +
                    "  \"password\": \"password_SlLVWDLl\"\n" +
                    "}\n";

    @Test
    public void createNewUserTest() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("------------- Test create user -------------");
        CreateUserResponse createUserResponse = userService.createUser(jsonCreateUser);
        Assert.assertNotNull(createUserResponse.result.id);
        Assert.assertNotNull(createUserResponse.result.countryId);
        System.out.printf("User id: %s%n",createUserResponse.result.id);
        System.out.printf("Country id: %s%n",createUserResponse.result.countryId);
    }

    @Test(dependsOnMethods={"createNewUserTest"})
    public void loginUserTest() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("------------- Test login user -------------");
        String jsonLoginUser = userService.getJsonLoginUser(jsonCreateUser);
        LoginUserResponse loginUserResponse = userService.loginUser(jsonLoginUser);

        Assert.assertNotNull(loginUserResponse.result.user.lastLoginDate);
        System.out.printf("Last login date: %s%n",loginUserResponse.result.user.lastLoginDate);
    }

    @Test(dependsOnMethods={"createNewUserTest"})
    public void createExistingUserTest() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("------------- Test create existing user -------------");
        CreateExistingUserResponse createUserResponse = userService.createExistingUser(jsonCreateUser);
        System.out.printf("Error code: %s%n", createUserResponse.result.error.code);
        System.out.printf("Error message: %s%n", createUserResponse.result.error.message);
        System.out.printf("Error object type: %s%n",createUserResponse.result.error.objectType);
    }

}
