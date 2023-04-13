package com.openx.fakestore;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;

public class TestGetRequest extends BaseTest {

    @Test
    public void testGetProductsRequest_ResponseStatusOK_Code200() {
        Response response = get("/products");
//        String actualBody = response.getBody().asString();
//        System.out.println(actualBody);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test (dataProvider = "products", dataProviderClass = DPClass.class)
    public void testSingleGetProductsRequest(int id, String expectedTitle ) {
        Response response = get("/products/" + id);
        String actualTitle = response.jsonPath().getString("title");
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test
    public void testGetUsersRequest_ResponseStatusOK_Code200() {
        Response response = get("/users");
//        String actualBody = response.getBody().asString();
//        System.out.println(actualBody);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test (dataProvider = "users", dataProviderClass = DPClass.class)
    public void testSingleGetUsersRequest(int id, String expectedFullname ) {
        Response response = get("/users/" + id);
        String actualFirstname = response.jsonPath().getString("name.firstname");
        String actualLastname = response.jsonPath().getString("name.lastname");
        String actualTitle = actualFirstname +" " + actualLastname;

        Assert.assertEquals(actualTitle, expectedFullname);
    }

    @Test
    public void testGetCartsRequest_ResponseStatusOK_Code200() {
        Response response = get("/carts");
//        String actualBody = response.getBody().asString();
//        System.out.println(actualBody);
        Assert.assertEquals(response.statusCode(), 200);
    }
}
