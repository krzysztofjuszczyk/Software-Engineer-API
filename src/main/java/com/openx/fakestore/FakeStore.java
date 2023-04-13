package com.openx.fakestore;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.openx.fakestore.carts.Cart;
import com.openx.fakestore.products.Product;
import com.openx.fakestore.users.User;


public class FakeStore {
    List<User> users = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<Cart> carts = new ArrayList<>();
    Map<String, Double> categoriesMap;

    public FakeStore() throws URISyntaxException, IOException, InterruptedException {
        retrieveProducts();
        retrieveUsers();
        retrieveCarts();
        categoriesMap = createCategoriesMap();
    }

    public static void main(String[] args) throws Exception {
        // 1. Retrieve user, product and shopping cart data
        FakeStore store1 = new FakeStore();
        System.out.println();

        // 2. Create a data structure of categories and their total value
        System.out.println(store1.categoriesMap.entrySet());

        //3. Find the cart with the highest value and its owner's full name
        Cart cartWithMaxValue = store1.findCartWithMaxValue();
        String maxCartFullName = store1.findUserById(cartWithMaxValue.getUserId()).getFullName();
        System.out.println("Cart with the highest value: " + store1.getCartValue(cartWithMaxValue) + " belongs to: " + maxCartFullName);

        // 4. find 2 users living furthest away from each other
        User[] result = store1.findUsersLivingFurthest();
        System.out.printf("2 users living the furthest away from each other are: %s and %s. \n",
                result[0].getFullName(),
                result[1].getFullName());
        System.out.printf("They live %.2f kms away.\n", result[0].getDistance(result[1]));
    }

    public void retrieveProducts() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getProductsRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_PRODUCTS))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getProductsResponse = httpClient.send(getProductsRequest, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        int responseCodeProducts = getProductsResponse.statusCode();

        if (responseCodeProducts == HttpURLConnection.HTTP_OK) {
            products = Arrays.asList(gson.fromJson(getProductsResponse.body(), Product[].class));
        } else {
            System.out.println("GET products request failed.");
        }
//        System.out.println(getProductsResponse.body());
    }

    public void retrieveUsers() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getUsersRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_USERS))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getUsersResponse = httpClient.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();

        int responseCodeUsers = getUsersResponse.statusCode();
        if (responseCodeUsers == HttpURLConnection.HTTP_OK) {
            users = Arrays.asList(gson.fromJson(getUsersResponse.body(), User[].class));
        } else {
            System.out.println("GET users request failed.");
        }
    }

    public void retrieveCarts() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getCartsRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.URL_CARTS))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getCartsResponse = httpClient.send(getCartsRequest, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        int responseCodeCarts = getCartsResponse.statusCode();
        if (responseCodeCarts == HttpURLConnection.HTTP_OK) {
            carts = Arrays.asList(gson.fromJson(getCartsResponse.body(), Cart[].class));
        } else {
            System.out.println("GET request failed.");
        }
    }


    public Map<String, Double> createCategoriesMap() {
        Map<String, Double> result = new HashMap<>();
        for (Product product : products) {
            String category = product.getCategory();
            Double value = result.getOrDefault(category, 0.0);
            value += product.getPrice();
            result.put(category, value);
        }
        return result;
    }

    public Product findProductById(int id) {
        for (Product product :
                products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public User findUserById(int id) {
        for (User user :
                users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public Cart findCartWithMaxValue() {
        Cart result = null;
        double maxCartValue = 0.0;
        for (Cart cart : carts) {
            double currentCartValue = getCartValue(cart);

            if (currentCartValue > maxCartValue) {
                result = cart;
                maxCartValue = currentCartValue;
            }
        }
        return result;
    }

    public double getCartValue(Cart cart) {
        double cartValue = 0.0;
        for (Cart.CartItem item : cart.getItems()) {
            double productPrice = findProductById(item.getProductId()).getPrice();
            cartValue += item.getQuantity() * productPrice;
        }
        return cartValue;
    }

    public User[] findUsersLivingFurthest() {
        User[] result = new User[2];
        double maxDistance = 0.0;
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                User user1 = users.get(i);
                User user2 = users.get(j);
                double distance = user1.getDistance(user2);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    result[0] = user1;
                    result[1] = user2;
                }
            }
        }
        return result;
    }
}


