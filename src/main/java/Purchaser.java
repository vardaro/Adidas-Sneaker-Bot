import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by justa on 4/20/2017.
 */
public class Purchaser {
  // Establish the user properties file
  private static final PropertiesReader user = new PropertiesReader("user.properties");
  private static final String FirstName = user.get("FirstName");
  private static final String LastName = user.get("LastName");
  private static final int Quantity = Integer.parseInt(user.get("Quantity"));
  private static final String Address = user.get("Address");
  private static final String City = user.get("City");
  private static final String State = user.get("State");
  private static final String ZipCode = user.get("ZipCode");
  private static final String PhoneNumber = user.get("PhoneNumber");
  private static final String Email = user.get("Email");
  private static final String CardType = user.get("CardType");
  private static final String CardNumber = user.get("CardNumber");
  private static final String ExpirationMonthNumber = user.get("ExpirationMonthNumber");
  private static final String ExpirationYearNumber = user.get("ExpirationYearNumber");
  private static final String CardSecurityCode = user.get("CardSecurityCode");
  private static String adidasalerts = "C:\\Users\\justa\\IdeaProjects\\SneakerBot\\Tweets\\adidasalerts.txt";
  //TODO: Shoe size is subject to change depending if shoe is True to Size (TTS)
  private static int ShoeSize = Integer.parseInt(user.get("ShoeSize"));

  public static void buyShoe(String url) {
    try {
      WebDriver driver = new ChromeDriver();
      driver.get("http://www.adidas.com/us/superstar-boost-shoes/BB0191.html");

      driver.findElement(By.className("ffSelectButtonPlaceholder")).click();
      driver.findElement(By.xpath("//*[@id=\"buy-block\"]/div[1]/div[5]/div[3]/form/div[2]/div[2]/div/div/div/div[2]/div/ul/li[14]/span")).click();

      WebDriverWait wait = new WebDriverWait(driver, 10);
      Thread.sleep(500);
      WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"buy-block\"]/div[1]/div[5]/div[3]/form/div[7]/div/div[1]/button/span")));
      addToCart.click();
      WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"minicart_overlay\"]/div[2]/a[2]")));
      checkoutButton.click();

      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_firstName\"]")).sendKeys(FirstName);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_lastName\"]")).sendKeys(LastName);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_address1\"]")).sendKeys(Address);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_phone\"]")).sendKeys(PhoneNumber);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_email_emailAddress\"]")).sendKeys(Email);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_city\"]")).sendKeys(City);
      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_singleshipping_shippingAddress_addressFields_zip\"]")).sendKeys(ZipCode);

      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery\"]/div[2]/div[2]/div/fieldset/div/div[1]/div[6]/div[1]/div/div/a/span")).click();


      // Create instance of Javascript executor
      JavascriptExecutor je = (JavascriptExecutor) driver;
      //Identify the WebElement which will appear after scrolling down
      WebElement element = driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery\"]/div[2]/div[2]/div/fieldset/div/div[1]/div[6]/div[1]/div/div/div/div[2]/div/ul/li[48]/span"));
      // now execute query which actually will scroll until that element is not appeared on page.
      je.executeScript("arguments[0].scrollIntoView(true);", element);
      element.click();
      Thread.sleep(250);

      driver.findElement(By.xpath("//*[@id=\"dwfrm_delivery_savedelivery\"]")).click();
      Thread.sleep(5000);

      // checkout info

      // card number
      driver.findElement(By.xpath("//*[@id=\"dwfrm_payment_creditCard_number\"]")).sendKeys(CardNumber);

      //cvv
      driver.findElement(By.xpath("//*[@id=\"dwfrm_payment_creditCard_cvn\"]")).sendKeys(CardSecurityCode);


//Identify the WebElement which will appear after scrolling down
      driver.findElement(By.xpath("//*[@id=\"dwfrm_payment\"]/fieldset/div/div[4]/div[2]/div/div/div/a/span")).click();
      WebElement month = driver.findElement(By.xpath("//*[@id=\"dwfrm_payment\"]/fieldset/div/div[4]/div[2]/div/div/div/div/div[2]/div/ul/li[2]/span"));

      je.executeScript("arguments[0].scrollIntoView(true);", month);
      element.click();

      driver.findElement(By.xpath("//*[@id=\"dwfrm_payment\"]/fieldset/div/div[4]/div[3]/div[1]/div/div/a/span")).click();
      WebElement year = driver.findElement(By.xpath("//*[@id=\"dwfrm_payment\"]/fieldset/div/div[4]/div[3]/div[1]/div/div/div/div[2]/div/ul/li[14]/span"));
      je.executeScript("arguments[0].scrollIntoView(true);", year);


      // place order
      driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div[4]/div/button/span")).click();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
