import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestHelper {

    static WebDriver driver;
    final int waitForResposeTime = 4;
	
	// here write a link to your admin website (e.g. http://my-app.herokuapp.com/admin)
    String baseUrlAdmin = "https://enigmatic-castle-18825.herokuapp.com/admin";
	
	// here write a link to your website (e.g. http://my-app.herokuapp.com/)
    String baseUrl = "https://enigmatic-castle-18825.herokuapp.com/";

    @Before
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\choi\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();

        // if you use Firefox:
        //System.setProperty("webdriver.gecko.driver", "C:\\Users\\...\\geckodriver.exe");
        //driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);
    }

    void goToPage(String page){
        WebElement elem = driver.findElement(By.linkText(page));
        elem.click();
        waitForElementById(page);
    }

    void waitForElementById(String id){
        new WebDriverWait(driver, waitForResposeTime).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    void login(String username, String password){

        driver.get(baseUrlAdmin);

        driver.findElement(By.linkText("Login")).click();

        driver.findElement(By.id("name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);


        By loginButtonXpath = By.xpath("//input[@value='Login']");

        WebElement login = driver.findElement(loginButtonXpath);
        login.click();
    }

    void logout(){
        WebElement logout = driver.findElement(By.linkText("Logout"));
        logout.click();

        waitForElementById("Admin");
    }

    void enterNewUserInfo(String username, String password) {
        driver.get(baseUrlAdmin);

        driver.findElement(By.linkText("Register")).click();

        driver.findElement(By.id("user_name")).sendKeys(username);
        driver.findElement(By.id("user_password")).sendKeys(password);
        driver.findElement(By.id("user_password_confirmation")).sendKeys(password);

        driver.findElement(By.xpath("//input[@value='Create User']")).click();
    }

    void register(String username, String password) {
        enterNewUserInfo(username, password);

        waitForElementById("notice");
    }

    void delete() {
        driver.findElement(By.linkText("Admin")).click();
        waitForElementById("Admin");

        driver.findElement(By.linkText("Delete")).click();
        waitForElementById("notice");
    }

    void addProduct(String title, String description, String type, String price) {
        waitForElementById("Products");
        driver.findElement(By.linkText("New product")).click();
        waitForElementById("product_title");

        driver.findElement(By.id("product_title")).sendKeys(title);
        driver.findElement(By.id("product_description")).sendKeys(description);

        Select select = new Select(driver.findElement(By.id("product_prod_type")));
        select.selectByVisibleText(type);

        driver.findElement(By.id("product_price")).sendKeys(price);

        driver.findElement(By.xpath("//input[@value='Create Product']")).click();
        waitForElementById("new_product_div");
    }

    boolean isProductAdded(String title) {
        // //*[@id="main"]/div/table
         List<WebElement> productRows = driver.findElements(By.xpath(("//*[@id=\"main\"]/div/table//tr")));
         productRows.remove(0);

         // //*[@id="B45593 Sunglasses"]/td[2]/a
         for (WebElement row: productRows) {
             WebElement rowTitle = row.findElement(By.xpath("./td[2]/a"));
             if (rowTitle.getText().equals(title)) return true;
         }

         return false;
    }

    void removeProduct(String title) {
        List<WebElement> productRows = driver.findElements(By.xpath(("//*[@id=\"main\"]/div/table//tr")));
        productRows.remove(0);

        for (WebElement row: productRows) {
            // //*[@id="B45593 Sunglasses"]/td[2]/a
            if (row.findElement(By.xpath("./td[2]/a")).getText().equals(title)) {
                // //*[@id="Airpod"]/td[4]/a
                row.findElement(By.xpath("./td[4]/a")).click();
                break;
            }
        }
    }

    void editProduct(String title, String type) {
        waitForElementById("Products");

        List<WebElement> productRows = driver.findElements(By.xpath(("//*[@id=\"main\"]/div/table//tr")));
        productRows.remove(0);

        for (WebElement row: productRows) {
            if (row.findElement(By.xpath("./td[2]/a")).getText().equals(title)) {
                row.findElement(By.xpath("./td[3]/a")).click();
                waitForElementById("product_title");

                Select select = new Select(driver.findElement(By.id("product_prod_type")));
                select.selectByVisibleText(type);

                driver.findElement(By.xpath("//input[@value='Update Product']")).click();

                break;
            }
        }
    }

    void addAProductToCart(String title) {
        // B45593 Sunglasses_entry
        // Sunglasses B45593_entry

        // //*[@id="Sunglasses B45593_entry"]
        WebElement div = driver.findElement(By.id(title));

        // //*[@id="B45593 Sunglasses_entry"]/div[2]/form/input[1]
        div.findElement(By.xpath("./div[2]/form/input[1]")).click();
    }

    void addToCart() {
        addAProductToCart("B45593 Sunglasses_entry");
        addAProductToCart("Sunglasses B45593_entry");
    }

    int getNumofRowInCart() {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));
        return cartRows.size();
    }

    void emptyCart() {
        try {
            driver.findElement(By.xpath("//input[@value='Empty cart']")).click();
        } catch(StaleElementReferenceException e) {
            driver.findElement(By.xpath("//input[@value='Empty cart']")).click();
        }
    }

    public WebElement getRow(String title) {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));

        for (WebElement row: cartRows) {
            boolean isMatch = false;
            int attempts = 0;

            while (attempts < 2) {
                try {
                    isMatch = row.findElement(By.xpath("./td[2]")).getText().equals(title);
                    attempts = 0;
                    break;
                } catch (StaleElementReferenceException e) {
                }

                attempts++;
            }

            if (isMatch) return row;
        }
        return null;
    }

    void removeAProductFromCart(String title) {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));

        for (WebElement row: cartRows) {
            boolean isMatch = false;
            int attempts = 0;

            while(attempts < 2) {
                try{
                    isMatch = row.findElement(By.xpath("./td[2]")).getText().equals(title);
                    attempts = 0;
                    break;
                } catch(StaleElementReferenceException e) {
                }

                attempts++;
            }

            if (isMatch) {
                while(attempts < 2) {
                    try{
                        row.findElement(By.xpath("./td[6]/a")).click();
                        attempts = 0;
                        break;
                    } catch(StaleElementReferenceException e) {
                    }

                    attempts++;
                }
                break;
            }
        }

    }

    public void increaseQuantity(String title) {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));

        for (WebElement row: cartRows) {
            boolean isMatch = false;
            int attempts = 0;

            while(attempts < 2) {
                try{
                    isMatch = row.findElement(By.xpath("./td[2]")).getText().equals(title);
                    attempts = 0;
                    break;
                } catch(StaleElementReferenceException e) {
                }

                attempts++;
            }

            if (isMatch) {
                while(attempts < 20) {
                    try{
                        // row.findElement(By.xpath("./td[6]/a")).click();
                        // //*[@id="cart"]/table/tbody/tr[1]/td[5]/a
                        row.findElement(By.xpath("./td[5]/a")).click();
                        attempts = 0;
                        break;
                    } catch(StaleElementReferenceException e) {
                    }

                    attempts++;
                }
                break;
            }
        }
    }

    public void decreaseQuantity(String title) {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));

        for (WebElement row: cartRows) {
            boolean isMatch = false;
            int attempts = 0;

            while(attempts < 2) {
                try{
                    isMatch = row.findElement(By.xpath("./td[2]")).getText().equals(title);
                    attempts = 0;
                    break;
                } catch(StaleElementReferenceException e) {
                }

                attempts++;
            }

            if (isMatch) {
                while(attempts < 20) {
                    try{
                        // row.findElement(By.xpath("./td[6]/a")).click();
                        // //*[@id="cart"]/table/tbody/tr[1]/td[5]/a
                        row.findElement(By.xpath("./td[4]/a")).click();
                        attempts = 0;
                        break;
                    } catch(StaleElementReferenceException e) {
                    }

                    attempts++;
                }
                break;
            }
        }
    }

    public boolean waitForIncDec(String title, int preQuantity, int isInd) {
        List<WebElement> cartRows = driver.findElements(By.xpath(("//*[@id=\"cart\"]/table//tr")));

        for (WebElement row: cartRows) {
            int attempts = 0;
            boolean isMatch = false;

            while(attempts < 2) {
                try{
                    isMatch = row.findElement(By.xpath("./td[2]")).getText().equals(title);
                    attempts = 0;
                    break;
                } catch(StaleElementReferenceException e) {
                }

                attempts++;
            }

            if (isMatch) {
                while(attempts < 2) {
                    try{
                        if (row.findElement(By.xpath("./td[1]")).getText().equals(Integer.toString(preQuantity + isInd) + "×")) return true;
                        else return false;
                    } catch(StaleElementReferenceException e) {
                    }

                    attempts++;
                }
                break;
            }
        }

        return false;
    }

    @After
    public void tearDown(){
        driver.close();
    }

}