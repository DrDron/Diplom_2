package Tests;

import Model.UserLogInData;
import Model.UserPostData;
import Service.UserService;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserLogInTest {
    private UserLogInData data;
    private Integer statusCode;
    private Boolean success;
    private String token;

    public UserLogInTest(UserLogInData data, Integer statusCode, Boolean success){
        this.data = data;
        this.statusCode = statusCode;
        this.success = success;
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {new UserLogInData("testusers@test.ru","testusers@test.ru"), 200, true},
                {new UserLogInData("@test.ru","testusers@test.ru"), 401, false},
                {new UserLogInData("testusers@test.ru","@test.ru"), 401, false},
        };
    }
    UserService userService = new UserService();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        token = userService.createUser(new UserPostData("testusers@test.ru","testusers@test.ru","testusers@test.ru"))
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
    }

    @Test
    public void userLogIn(){
        userService.userLogIn(data)
                .then()
                .assertThat()
                .statusCode(statusCode)
                .body("success", equalTo(success));
    }

    @After
    public void clear() {
        userService.deleteUser(token);
    }
}
