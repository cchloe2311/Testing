import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicTest extends TestHelper {


    private String username = "Seojeong";
    private String password = "2016112148";
    private String wrongPassword = "2016 112 148";

    private String newUsername = "Chloe";
    private String newPassword = "19964029";

    private String productTitle = "Airpod";
    private String productDescription = "You should buy it";
    private String productCategory = "Other";
    private String productPrice = "200";

    private String productTitleEdit = "Galaxy buds";
    private String productTypeEdit = "Sunglasses";

    private String theProductTitle = "B45593 Sunglasses";

    private String[] categories = {"Sunglasses", "Books", "Other"};

    private String totalAmount = "€104.00";

    //@Test
    public void titleExistsTest(){
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }


    /*
    In class Exercise

    Fill in loginLogoutTest() and login mehtod in TestHelper, so that the test passes correctly.

     */
    //@Test
    public void loginLogoutTest(){

        login(username, password);

        try{
            WebElement adminHeader = driver.findElement(By.className("admin_header"));
            assertFalse(true);
        } catch(NoSuchElementException e) {
            assertTrue(true);
        }

        logout();

        try{
            WebElement adminHeader = driver.findElement(By.className("admin_header"));
            assertTrue(true);
        } catch(NoSuchElementException e) {
            assertFalse(true);
        }
    }

   // @Test
    public void wrongPasswordTest() {
        login(username, wrongPassword);

        try{
            WebElement loginHeader = driver.findElement(By.id("login_header"));
            assertTrue(true);
        } catch(NoSuchElementException e) {
            assertFalse(true);
        }
    }

    //@Test
    public void registerDeleteTest() {
        register(newUsername, newPassword);

        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User " + newUsername + " was successfully created.",notice.getText());

        delete();

        notice = driver.findElement(By.id("notice"));
        assertEquals("User was successfully deleted.", notice.getText());
    }

    //@Test
    public void registerFailureTest() {
        enterNewUserInfo(username, password);

        WebElement error = driver.findElement(By.id("error_explanation"));
        assertEquals("1 error prohibited this user from being saved:\n" +
                "Name has already been taken", error.getText());

    }

    //@Test
    public void addRemoveProductTest() {
        login(username, password);

        addProduct(productTitle, productDescription, productCategory, productPrice);
        waitForElementById("new_product_div");
        assertTrue(isProductAdded(productTitle));

        removeProduct(productTitle);
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Product was successfully destroyed.",notice.getText());

        logout();
    }

    @Test
    public void addProductFailureTest() {
        login(username, password);

        addProduct(productTitleEdit, productDescription, productCategory, productPrice);
        waitForElementById("error_explanation");

        assertEquals("Title has already been taken", driver.findElement(By.xpath("//*[@id=\"error_explanation\"]/ul/li")).getText());
    }

    //@Test
    public void editProductTest() {
        login(username, password);

        editProduct(productTitleEdit, productTypeEdit);
        WebElement type = driver.findElement(By.xpath("//*[@id=\"main\"]/div/p[4]"));
        assertEquals("Type: " + productTypeEdit, type.getText());

        logout();
    }

    //@Test
    public void addToCartTest() {
        addToCart();

        int postNumOfRow = getNumofRowInCart();

        assertEquals( 2, postNumOfRow);
    }

    //@Test
    public void emptyCartTest() {
        addToCart();
        emptyCart();

        try{
            WebElement cartTitle = driver.findElement(By.id("cart_title"));
            assertFalse(true);
        } catch(NoSuchElementException e) {
            assertTrue(true);
        }
    }

    //@Test@
    public void removeAProductTest() throws InterruptedException {
        addToCart();
        removeAProductFromCart(theProductTitle);

        // waitForElementById를 쓸 수 없어서 이렇게 구현했습니다.
        while(true) {
            int postNumOfRow = getNumofRowInCart();
            if ((3 - 1) == postNumOfRow) break;
        }

        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Item successfully deleted from cart.",notice.getText());
    }

    //@Test
    public void incDecInQuantityTest() throws InterruptedException {
        addToCart();

        final int INC = 1;
        final int DEC = 2;

        changeQuantity(theProductTitle, INC);
        assertTrue(isQuantityChanged(theProductTitle, 1, +1));

        changeQuantity(theProductTitle, DEC);
        assertTrue(isQuantityChanged(theProductTitle, 1, -1));
    }

    //@Test
    public void searchByTitleTest() {
        searchByTitle(theProductTitle);
        assertTrue(isSearchByTitleWorks(theProductTitle));
    }

    //@Test
    public void searchByTitleFailureTest() {
        searchByTitle("someThingNowExist");
        assertTrue(isSearchByTitleWorks("someThingNowExist"));
    }

    //@Test
    public void payProductsTest() {
        // 같은 물품을 여러 개 추가하기 위해 addToCart 메소드를 두번 호출
        addToCart();
        addToCart();

        payProducts();

        assertEquals(totalAmount, driver.findElement(By.xpath("//*[@id=\"check_out\"]/tbody/tr[3]/td[2]/strong")).getText());
    }

    //@Test
    public void payProductFailureTest() {
        try {
            payProducts();
            assertFalse(true);
        } catch(NoSuchElementException e) {
            assertTrue(true);
        }
    }

    //@Test
    public void filterByCategoryTest() {
        for(String category: categories) {
            filterByCategory(category);
            assertTrue(isFilterByCategoryWorks(category));
        }
    }
}
