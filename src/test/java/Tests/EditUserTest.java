package Tests;

import Model.UserLogInData;
import Model.UserPostData;
import Service.UserService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class EditUserTest {
    private Integer statusCode;
    private Boolean success;
    private UserPostData userEditData;

    public EditUserTest(UserPostData userEditData, Integer statusCode, Boolean success){
        this.userEditData = userEditData;
        this.statusCode = statusCode;
        this.success = success;
    }
    private UserPostData userPostData = new UserPostData("users10@test.ru","users10@test.ru","users10");

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {new UserPostData("newusers@test.ru","newusers@test.ru","newUsers"),200, true},
                {new UserPostData("newusers@test.ru","newusers@test.ru",null),200, true},
                {new UserPostData("newusers@test.ru",null,null),200, true},
                {new UserPostData(null,"newusers@test.ru",null),200, true},
                {new UserPostData(null,null,"newUsers"),200, true},
                {new UserPostData("newusers@test.ru","newusers@test.ru","newUsers"),401, false},
        };
    }
    UserService userService = new UserService();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void editUser(){
        String accessToken = userService.createUser(userPostData)
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        if (success){
            userService.userLogIn(new UserLogInData("users@test.ru","users@test.ru"));
            userService.editUserData(userEditData,accessToken)
                    .then()
                    .assertThat()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
        }
        else{
            userService.editUserData(userEditData,"")
                    .then()
                    .assertThat()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
        }

        userService.userLogOut(accessToken);
        userService.deleteUser(accessToken);
    }

}
