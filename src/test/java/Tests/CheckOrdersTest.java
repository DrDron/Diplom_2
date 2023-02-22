package Tests;

import Model.OrderPostData;
import Model.UserLogInData;
import Model.UserPostData;
import Service.OrderService;
import Service.UserService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CheckOrdersTest {
    private Boolean isAuthorized;
    private Integer statusCode;
    private Boolean success;

    public CheckOrdersTest(Boolean isAuthorized, Integer statusCode, Boolean success) {
        this.isAuthorized = isAuthorized;
        this.statusCode = statusCode;
        this.success = success;
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {false, 401, false},
                {true, 200, true},

        };
    }

    OrderService orderService = new OrderService();
    UserService userService = new UserService();
    final static String USER_DATA = "userForOrder25@test.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void checkOrders(){
        String accessToken = userService.createUser(new UserPostData(USER_DATA, USER_DATA, USER_DATA))
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        if(isAuthorized) {
            userService.userLogIn(new UserLogInData(USER_DATA, USER_DATA));
            orderService.createOrder(new OrderPostData(List.of("61c0c5a71d1f82001bdaaa6d")),accessToken);
            orderService.getUserOrders(accessToken)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
        }
        else {
            orderService.getUserOrdersWithoutToken()
                .then()
                .log().all()
                .assertThat()
                .statusCode(statusCode)
                .body("success", equalTo(success));
        }

        userService.userLogOut(accessToken);
        userService.deleteUser(accessToken);
    }
}
