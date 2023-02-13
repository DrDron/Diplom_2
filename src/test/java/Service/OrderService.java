package Service;

import Model.OrderPostData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderService {

    @Step("Send POST request to /api/orders (Создать заказ)")
    public Response createOrder(OrderPostData data){
        return  given()
                .header("Content-type", "application/json")
                .body(data)
                .post("/api/orders");
    }

    @Step("Send GET request to /api/orders (Получить список заказов)")
    public Response getOrders(){
        return  given()
                .header("Content-type", "application/json")
                .get("/api/orders");
    }

}
