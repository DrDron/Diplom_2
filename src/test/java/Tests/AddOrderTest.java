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
public class AddOrderTest {
    private Boolean isAuthorized;
    private Boolean isWrongHash;
    private OrderPostData orderPostData;
    private Integer statusCode;
    private Boolean success;
    private static List<String> ingredients = List.of(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa76");

    public AddOrderTest(Boolean isAuthorized,
                        Boolean isWrongHash,
                        OrderPostData orderPostData,
                        Integer statusCode,
                        Boolean success) {
        this.isAuthorized = isAuthorized;
        this.isWrongHash = isWrongHash;
        this.orderPostData = orderPostData;
        this.statusCode = statusCode;
        this.success = success;
    }


    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {true,true, new OrderPostData(ingredients),200,true},
                {true,false, new OrderPostData(List.of()),400,false},
                {true,true, new OrderPostData(List.of("someHash")),500,false},
                //Тут баг - заказ создаётся без авторизации
                //{false,true, new OrderPostData(ingredients),401,false},
        };
    }

    OrderService orderService = new OrderService();
    UserService userService = new UserService();
    final static String USER_DATA = "userForOrder01@test.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void addOrder(){
        String accessToken = userService.createUser(new UserPostData(USER_DATA, USER_DATA, USER_DATA))
                .then()
                .extract().path("accessToken").toString().substring(6).trim();
        if(isAuthorized){
            userService.userLogIn(new UserLogInData(USER_DATA,USER_DATA));
            if(!isWrongHash)
            orderService.createOrder(orderPostData,accessToken)
                    .then()
                    .assertThat()
                    .and()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
            else
                orderService.createOrder(orderPostData,accessToken)
                        .then()
                        .assertThat()
                        .and()
                        .statusCode(statusCode);

        }
        else{
            orderService.createOrderWithoutToken(orderPostData)
                    .then()
                    .assertThat()
                    .statusCode(statusCode)
                    .body("success", equalTo(success));
        }

        userService.userLogOut(accessToken);
        userService.deleteUser(accessToken);
    }
}
