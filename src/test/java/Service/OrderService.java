package Service;

import Model.OrderPostData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderService {

    @Step("Send POST request to /api/orders (Создать заказ)")
    public Response createOrder(OrderPostData orderPostData, String accessToken){
        return  given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(orderPostData)
                .when()
                .post("/api/orders");
    }

    @Step("Send POST request to /api/orders without token(Создать заказ без авторизации)")
    public Response createOrderWithoutToken(OrderPostData orderPostData){
        return  given()
                .header("Content-type", "application/json")
                .body(orderPostData)
                .when()
                .post("/api/orders");
    }

    @Step("Send GET request to /api/orders (Получить список заказов)")
    public Response getUserOrders(String accessToken){
        return  given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .get("/api/orders");
    }
    @Step("Send GET request to /api/orders without token (Получить список заказов без авторизации)")
    public Response getUserOrdersWithoutToken(){
        return  given()
                .header("Content-type", "application/json")
                .get("/api/orders");
    }

}
