package com.openx.fakestore;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import com.openx.fakestore.carts.Cart;
import com.openx.fakestore.products.Product;
import com.openx.fakestore.users.User;


public class FakeStore {


    public static void main(String[] args) throws Exception {
        List<User> users = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Cart> carts = new ArrayList<>();

        HttpRequest getUsersRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_USERS))
                .build();
        HttpRequest getProductsRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_PRODUCTS))
                .build();
        HttpRequest getCartsRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_CARTS))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getUsersResponse = httpClient.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> getProductsResponse = httpClient.send(getProductsRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> getCartsResponse = httpClient.send(getCartsRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(getUsersResponse.body());
        System.out.println(getProductsResponse.body());
        System.out.println(getCartsResponse.body());

        Gson gson = new Gson();

        // USERS
        int responseCode = getUsersResponse.statusCode();
//            User user = gson.fromJson(getUsersResponse, User.class);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            users = gson.fromJson(getUsersResponse.body(), new TypeToken<List<User>>() {
            }.getType());
            for (User user : users) {
                System.out.println(user.getId() + user.getUsername());
            }
        } else {
            System.out.println("GET request failed.");
        }

        // PRODUCTS

        int responseCodeProducts = getProductsResponse.statusCode();

        if (responseCodeProducts == HttpURLConnection.HTTP_OK) {
            products = gson.fromJson(getProductsResponse.body(), new TypeToken<List<Product>>() {
            }.getType());
            for (Product product : products) {
                System.out.println(product.getId() + product.getCategory());
            }
        } else {
            System.out.println("GET request failed.");
        }
        System.out.println();

        // CARTS
        int responseCodeCarts = getProductsResponse.statusCode();

        if (responseCodeCarts == HttpURLConnection.HTTP_OK) {
            carts = gson.fromJson(getCartsResponse.body(), new TypeToken<List<Cart>>() {
            }.getType());
            for (Cart cart : carts) {

            }
        } else {
            System.out.println("GET request failed.");
        }
        System.out.println();

        // 2. Create a data structure of categories and their total value
        Map<String, Double> categoryTotalSum = new HashMap<>();
        for (Product product : products) {
            String category = product.getCategory();
            Double value = categoryTotalSum.getOrDefault(category, 0.0);
            value += product.getPrice();
            categoryTotalSum.put(category, value);
        }
        System.out.println(categoryTotalSum.entrySet());

        //3. Find the cart with the highest value and its owner's full name
        double maxCartValue = 0.0;
        String maxCartFullName = null;
        for (Cart cart : carts) {
            double cartValue = 0.0;
            for (Cart.CartItem item : cart.getItems()) {
                double productPrice = findProductById(item.getProductId(), products).getPrice();
                cartValue += item.getQuantity() * productPrice;
            }
            if (cartValue > maxCartValue) {
                maxCartValue = cartValue;
                maxCartFullName = findUserById(cart.getUserId(), users).getFullName();
            }
        }
        System.out.println("Cart with the highest value: " + maxCartValue + " belongs to: " + maxCartFullName);


        //        4. find 2 users living furthest away from each other
// Find the two users living furthest away from each other
        double maxDistance = 0.0;
        User furthestUser1 = null;
        User furthestUser2 = null;
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                User user1 = users.get(i);
                User user2 = users.get(j);
                double distance = user1.getDistance(user2);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    furthestUser1 = user1;
                    furthestUser2 = user2;
                }
            }
        }
        System.out.printf("2 users living the furthest away from each other are: %s and %s. \n",
                furthestUser1.getFullName(),
                furthestUser2.getFullName());
        System.out.printf("They live %.2f kms away.\n", maxDistance);
    }


    public static Product findProductById(int id, List<Product> products) {
        for (Product product :
                products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public static User findUserById(int id, List<User> users) {
        for (User user :
                users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }


}


