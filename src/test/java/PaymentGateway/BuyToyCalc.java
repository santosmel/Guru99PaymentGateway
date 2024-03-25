package PaymentGateway;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.DecimalFormat;

public class BuyToyCalc {

    WebDriver driver;

    @Parameters({"url"})
    @BeforeTest
    public void setUp(String url) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
    }

    @DataProvider (name="data-provider")
    public Object[][] qtyNumber() {
        Object[][] data = new Object[3][2];

        data[0][0] = "//div/select/option[@value='1']";
        data[0][1] = "20";

        data[1][0] = "//div/select/option[@value='2']";
        data[1][1] = "20";

        data[2][0] = "//div/select/option[@value='3']";
        data[2][1] = "20";

        return data;
    }
    @Test (dataProvider = "data-provider")
    public void selectQuantity(String sqty, String price){

        WebElement quantity = driver.findElement(By.xpath(sqty));
        quantity.click();

        WebElement buyNow = driver.findElement(By.xpath("//input[@type='submit']"));
        buyNow.click();

        String qtyXpath = sqty.substring(28,29);
        System.out.println("Quantity: " + qtyXpath);

        // Retrieve system Payment Amount
        String payAmount = driver.findElement(By.xpath("//div/font[@color='red']")).getText().trim();
        System.out.println("Pay Amount: " + payAmount);

        // Calculate Amount
        Double qtyNumeric = Double.valueOf(price) * Double.valueOf(qtyXpath);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        String compAmount = "$" + decimalFormat.format(qtyNumeric);
        System.out.println("Computed Amount: " + compAmount);

        Assert.assertEquals(payAmount, compAmount);

        //Back to Home page
        driver.findElement(By.linkText("Guru99 Payment Gateway")).click();
        };

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}


