import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by justa on 4/20/2017.
 *
 * Buys shoe with selenium
 */
class Purchaser {
  // Establish the user properties file
  private static PropertiesReader user = new PropertiesReader("user.properties");
  private static PropertiesReader form = new PropertiesReader("adidas.properties");
  private static WebDriver driver = new ChromeDriver();
  private static WebDriverWait wait = new WebDriverWait(driver, 10);

  // JavaScript function stored in string
  // Called in JavascriptExecutor
  private static final String jsScrollFunction = "arguments[0].scrollIntoView(true);";

  static void buyShoe(String url) {
    try {

      // Go to webpage
      driver.get("http://www.adidas.com/us/superstar-boost-shoes/BB0191.html");

      /*WebElement heck = driver.findElement(By.xpath("/*//*[contains(text(), '12')]"));
      System.out.println(heck.getText());
      if (heck.getText().equals("12")) {
        System.out.println("yes boyyyyy");
      } else {
        System.out.println("no boysss");
      }*/

      // Handle possibility of dropdown menu of slide
      if (Boolean.parseBoolean(form.get("SizeSelectionIsDropdown"))) {
        executeDropdown(form.get("DropdownInstructions").split(","));
      } else {
        // Normal non-dropdown instructions
        //xpathFill(form.get("DropdownInstructions"));
      }

      if (Boolean.parseBoolean(form.get("QuantitySelectionIsDropdown"))) {
        executeDropdown(form.get("QuantityInstructions").split(","));
      } else {
        // Normal size selection
        // xpathFill(form.get("QuantityInstructions"));
      }

      xpathClick(form.get("AddToCart"));
      Thread.sleep(1000);
      xpathClick(form.get("Checkout"));
      Thread.sleep(1000);
      xpathFill(form.get("FirstName"),user.get("FirstName"));
      xpathFill(form.get("LastName"),user.get("LastName"));
      xpathFill(form.get("Address"),user.get("Address"));
      xpathFill(form.get("PhoneNumber"),user.get("PhoneNumber"));
      xpathFill(form.get("Email"),user.get("Email"));
      xpathFill(form.get("City"),user.get("City"));
      xpathFill(form.get("ZipCode"),user.get("ZipCode"));
      executeDropdown(form.get("State").split(","));
      xpathClick(form.get("ReviewAndPay"));
      Thread.sleep(5000);

      // checkout info

      xpathFill(form.get("CardNumber"),user.get("CardNumber"));
      xpathFill(form.get("SecurityCode"),user.get("CardSecurityCode"));
      executeDropdown(form.get("CardMonth").split(","));
      executeDropdown(form.get("CardYear").split(","));
      xpathClick(form.get("PlaceOrderButton"));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Fills in a form input on the webdriver based on xpath
   * @param inputElement The input element on the form
   * @param valueInputted The value going into the inputElement
   */
  private static void xpathFill(String inputElement, String valueInputted) {
    driver.findElement(By.xpath(inputElement)).sendKeys(valueInputted);
  }

  /**
   * Clicks on an element on the webpage based on .
   * If the element is not clickable because it's covered
   * up by something else, it'll wait until it's clickable
   * and timeout after 10 seconds
   *
   * @param elementToClick Element you want to click
   */
  private static void xpathClick(String elementToClick) {
      WebElement element = driver.findElement(By.xpath(elementToClick));
      if (!element.isDisplayed() && !element.isEnabled()) {
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript(jsScrollFunction,element);
      }
      element.click();
  }

  /**
   * Method made specifically for dropdown menus
   *
   * The corresponding properties file of the website
   * contains a sequence of xpaths tailored for that
   * dropdown.
   *
   * The method runs through each xpath,
   * however if the element is not in view,
   * a WebDriver exception will be caught and
   * it will scroll down until it's clickable
   *
   * @param instructions Sequence of xpaths for dropdown
   */
  private static void executeDropdown(String[] instructions) {
    JavascriptExecutor je = (JavascriptExecutor) driver;
    WebElement element;

    // Loop through each step in array
    for (String step : instructions) {
      element = driver.findElement(By.xpath(step));
      try {
        element.click();
      } catch (WebDriverException wde) {
        // Occurs when the element must be scrolled into view before being clickable
        je.executeScript(jsScrollFunction,element);
        element.click();
      }
    }
  }
}
