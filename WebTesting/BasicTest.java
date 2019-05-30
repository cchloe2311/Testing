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
    private String productType = "Other";
    private String productPrice = "200";

    private String productTitleEdit = "Galaxy buds";
    private String productTypeEdit = "Sunglasses";

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

        addProduct(productTitle, productDescription, productType, productPrice);
        assertTrue(isProductAdded(productTitle));

        removeProduct(productTitle);
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Product was successfully destroyed.",notice.getText());

        logout();
    }

    @Test
    public void editProductTest() {
        login(username, password);

        editProduct(productTitleEdit, productTypeEdit);
        WebElement type = driver.findElement(By.xpath("//*[@id=\"main\"]/div/p[4]"));
        assertEquals("Type: " + productTypeEdit, type.getText());

        logout();
    }

}
