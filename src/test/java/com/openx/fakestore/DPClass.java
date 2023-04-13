package com.openx.fakestore;

import org.testng.annotations.DataProvider;

public class DPClass {
    @DataProvider(name = "categories")
    public static Object[][] categories() {
        return new Object[][]{
                {"electronics", 1994.99},
                {"women's clothing", 157.72},
                {"men's clothing", 204.23},
                {"jewelery", 883.98}
        };
    }

    @DataProvider(name = "users")
    public static Object[][] users() {
        return new Object[][]{
                {1, "john doe"},
                {2, "david morrison"},
                {9, "kate hale"},
                {10, "jimmie klein"}
        };
    }

    @DataProvider(name = "products")
    public static Object[][] products() {
        return new Object[][]{
                {1, "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"},
                {2, "Mens Casual Premium Slim Fit T-Shirts "},
                {19, "Opna Women's Short Sleeve Moisture"},
                {20, "DANVOUY Womens T Shirt Casual Cotton Short"}
        };
    }

    @DataProvider(name = "carts")
    public static Object[][] carts() {
        return new Object[][]{
                {1, 798.04},
                {2, 2578.7},
                {7, 9.85}
        };
    }

}
