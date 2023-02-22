package Service;

import Model.UserLogInData;
import Model.UserPostData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserService {

    @Step("Send POST request to /api/auth/register (Создать пользователя)")
    public Response createUser(UserPostData data){
        return  given()
                .header("Content-type", "application/json")
                .body(data)
                .post("/api/auth/register");
    }

    @Step("Send POST request to /api/auth/login (Авторизация пользователя)")
    public Response userLogIn(UserLogInData data){
        return  given()
                .header("Content-type", "application/json")
                .body(data)
                .post("/api/auth/login");
    }

    @Step("Send POST request to /api/auth/logout (Выйти из системы)")
    public Response userLogOut(String  refreshToken){
        return  given()
                .header("Content-type", "application/json")
                .body(refreshToken)
                .post("/api/auth/logout");
    }

    @Step("Send GET request to /api/auth/user (Получение информации о пользователе)")
    public Response getUserData(String authorization){
        return  given()
                .header("Content-type", "application/json")
                .body(authorization)
                .get("/api/auth/logout");
    }

    @Step("Send PATCH request to /api/auth/user (Обновление информации о пользователе)")
    public Response editUserData(UserPostData data, String  accessToken){
        return  given()
                .header("authorization", "bearer " + accessToken)
                .body(data)
                .patch("/api/auth/user");
    }

    @Step("Send DELETE request to /api/auth/user (Удалить пользователя)")
    public Response deleteUser(String  accessToken){
        return  given()
                .header("authorization", "bearer " + accessToken)
                .delete("/api/auth/user");
    }
}
