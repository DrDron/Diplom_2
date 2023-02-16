package Tests;

import Model.UserPostData;
import Service.UserService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class AddUserTest {
    private UserPostData data;
    private Integer statusCode;
    private Boolean success;

    public AddUserTest(UserPostData data, Integer statusCode, Boolean success){
        this.data = data;
        this.statusCode = statusCode;
        this.success = success;
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {new UserPostData("userss@test.ru","userss@test.ru","userss"), 200, true},
                {new UserPostData("admin@test.ru","admin@test.ru","admin"), 403, false},
                {new UserPostData("user1@test.ru","","admin"), 403, false},
                {new UserPostData("user2@test.ru","admin@test.ru",""), 403, false},
                {new UserPostData("user2@test.ru","admin@test.ru",""), 403, false},
        };
    }
    UserService userService = new UserService();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void createUser(){
        if (success){
        String accessToken = userService.createUser(data)
                .then()
                .assertThat()
                .statusCode(statusCode)
                .body("success", equalTo(success))
                .extract().path("accessToken").toString().substring(6).trim();

                userService.userLogOut(accessToken);
                userService.deleteUser(accessToken);
        }
        else
            userService.createUser(data)
                    .then()
                    .assertThat()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
    }

}
