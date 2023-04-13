package com.openx.fakestore;

import io.restassured.RestAssured;

public class BaseTest {
    public BaseTest() {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }
}
