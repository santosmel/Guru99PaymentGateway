package PaymentGateway;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.DecimalFormat;

public class BuyToyParams {

    WebDriver driver;

    @Parameters({"url"})
    @BeforeClass
    public void setUp(String url) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
    }

    @Parameters({"qty"})
    @Test(priority = 100)
    public void selectQuantity(String sqty){
        WebElement quantity = driver.findElement(By.xpath(sqty));
        quantity.click();

        WebElement buyNow = driver.findElement(By.xpath("//input[@type='submit']"));
        buyNow.click();
    }


    @Test(priority = 200)
    public String retrievePayAmount() {
        String payAmount = driver.findElement(By.xpath("//div/font[@color='red']")).getText().trim();
        System.out.println("Pay Amount: " + payAmount);
        return payAmount;
    }

    @Parameters({"qty", "price"})
    @Test(priority = 300)
    public void verifyPayAmount(String qtyXpath, Integer price) {
        String qtyNum = qtyXpath.substring(28,29);

        Double qtyNumeric = price * Double.valueOf(qtyNum);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        String compAmount = "$" + decimalFormat.format(qtyNumeric);
        System.out.println("Computed Amount: " + compAmount);

        Assert.assertEquals(retrievePayAmount(), compAmount);
    }

    @Parameters({"card", "month", "year", "cvv"})
    @Test(priority = 400)
    public void provideCard(String card, String month, String year, String cvv) {

        driver.findElement(By.name("card_nmuber")).sendKeys(card);
        driver.findElement(By.xpath(month)).click();
        driver.findElement(By.xpath(year)).click();
        driver.findElement(By.id("cvv_code")).sendKeys(cvv);

        driver.findElement(By.name("submit")).click();

        String orderId = driver.findElement(By.xpath("//table[@class='alt access']/tbody/tr[1]/td[2]/h3/strong")).getText();

        System.out.println("Order ID: " + orderId);

    }

    @Test(priority = 500)
    public void tearDown() {
        driver.quit();
    }


}
