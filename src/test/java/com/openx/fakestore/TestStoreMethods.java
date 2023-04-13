package com.openx.fakestore;

import com.openx.fakestore.carts.Cart;
import com.openx.fakestore.users.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestStoreMethods extends BaseTest {
    private FakeStore testStore;

    @BeforeClass
    public void setup()
            throws URISyntaxException, IOException, InterruptedException {
        testStore = new FakeStore();
        System.out.println("test store created in TestStoreMethods class");
    }

    @Test(dataProvider = "categories", dataProviderClass = DPClass.class)
    public void testStore_CategoriesShouldHaveCorrectValues(String category, double expectedValue) {
        Assert.assertEquals(testStore.categoriesMap.size(), 4);
        Assert.assertEquals(testStore.categoriesMap.get(category), expectedValue, 0.001);
    }

    @Test(dataProvider = "users", dataProviderClass = DPClass.class)
    public void testStore_UsersShouldHaveCorrectName(int id, String expectedFullname) {
        Assert.assertEquals(testStore.users.size(), 10, "incorrect users list size");

        String actualFullname = testStore.findUserById(id).getFullName();
        Assert.assertEquals(actualFullname, expectedFullname);
    }

    @Test(dataProvider = "products", dataProviderClass = DPClass.class)
    public void testStore_ProductsShouldHaveCorrectId(int id, String expectedTitle) {
        Assert.assertEquals(testStore.products.size(), 20, "incorrect products list size");

        String actualFullname = testStore.findProductById(id).getTitle();
        Assert.assertEquals(actualFullname, expectedTitle);
    }

    @Test(dataProvider = "carts", dataProviderClass = DPClass.class)
    public void testStore_CartsShouldHaveCorrectValue(int cartId, double expectedValue) {
        Assert.assertEquals(testStore.carts.size(), 7, "incorrect carts list size");

        double actualValue = testStore.getCartValue(testStore.carts.get(cartId - 1)); //cart index = id-1;
        Assert.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void testStore_FindCartWithMaxValue() {
        Cart actualCartWithMaxValue = testStore.findCartWithMaxValue();
        double actualCartValue = testStore.getCartValue(actualCartWithMaxValue);
        int actualId = actualCartWithMaxValue.getUserId();
        String actualFullname = testStore.findUserById(actualId).getFullName();

        Assert.assertEquals(actualCartValue, 2578.7, "incorrect cart value");
        Assert.assertEquals(actualId, 1, "incorrect id");
        Assert.assertEquals(actualFullname, "john doe", "incorrect fullname");
    }

    @Test
    public void testStore_findUsersLivingFurthest() {
        User[] actualUsersLivingFurthest = testStore.findUsersLivingFurthest();
        double actualDistance = actualUsersLivingFurthest[0].getDistance(actualUsersLivingFurthest[1]);

        Assert.assertEquals(actualDistance, 15012.06, 0.01);
        Assert.assertEquals(actualUsersLivingFurthest[0].getId(), 1);
        Assert.assertEquals(actualUsersLivingFurthest[1].getId(), 5);
    }
}
