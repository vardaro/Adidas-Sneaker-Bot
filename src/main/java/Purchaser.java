import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;

/**
 * Created by justa on 4/20/2017.
 * <p>
 * Buys shoe with selenium
 * TODO: do while loop
 * TODO: make execute dropdown decision method implrment
 */

//http://www.adidas.com/us/alphabounce-reigning-champ-shoes/CG4301.html
class Purchaser {
  // JavaScript function stored in string
  // Called in JavascriptExecutor
  private static final String jsScrollFunction = "arguments[0].scrollIntoView(true);";

  private static PropertiesReader user = new PropertiesReader("user.properties");
  private static PropertiesReader form = new PropertiesReader("adidas.properties");
  private static WebDriver driver = new ChromeDriver();
  private static String purchaseTxt = "C:\\Users\\justa\\IdeaProjects\\SneakerBot\\Purchase History.txt";

  /**
   * Fills out the checkout form
   *
   * @param url url of the shoe being bought
   */
  static void buyShoe(String url) {

    try {
      // To prevent buying the shoe again
      if (notCopped(url)) {
        // Go to webpage
        driver.get(url);
        //driver.get("http://www.adidas.com/us/alphabounce-reigning-champ-shoes/CG4301.html");

        executePossibleDropdown("DropdownInstructions", "ShoeSize", "SizeSelectionIsDropdown");

        executePossibleDropdown("QuantityInstructions", "Quantity", "QuantitySelectionIsDropdown");

        captcha();

        xpathClick(form.get("AddToCart"));
        Thread.sleep(1500);
        xpathClick(form.get("Checkout"));
        Thread.sleep(1500);


        // If they prompt login
        if (driver.getCurrentUrl().equals("https://www.adidas.com/us/checkout-start")) {
          driver.findElement(By.xpath("//*[@id=\"frameContainer\"]/div[2]/form/div/button")).click();
        }

        xpathFill(form.get("FirstName"), user.get("FirstName"));
        xpathFill(form.get("LastName"), user.get("LastName"));
        xpathFill(form.get("Address"), user.get("Address"));
        xpathFill(form.get("PhoneNumber"), user.get("PhoneNumber"));
        xpathFill(form.get("Email"), user.get("Email"));
        xpathFill(form.get("City"), user.get("City"));
        xpathFill(form.get("ZipCode"), user.get("ZipCode"));

        executePossibleDropdown("StateInstructions", "State", "StateIsDropdown");

        // Billing info
        xpathClick("//*[@id=\"dwfrm_delivery\"]/div[2]/div[2]/div/fieldset/div/div[2]/div/div/span");
        xpathFill(form.get("BillingFirstName"), user.get("BillingFirstName"));
        xpathFill(form.get("BillingLastName"), user.get("BillingLastName"));
        xpathFill(form.get("BillingAddress"), user.get("BillingAddress"));
        xpathFill(form.get("BillingCity"), user.get("BillingCity"));
        xpathFill(form.get("BillingZipCode"), user.get("BillingZipCode"));
        xpathFill(form.get("BillingPhoneNumber"), user.get("BillingPhoneNumber"));

        executePossibleDropdown("BillingStateInstructions", "State", "BillingStateIsDropdown");


        xpathClick(form.get("ReviewAndPay"));
        Thread.sleep(3000);

        // checkout info
        xpathFill(form.get("CardNumber"), user.get("CardNumber"));
        xpathFill(form.get("SecurityCode"), user.get("CardSecurityCode"));

        executePossibleDropdown("CardMonthInstructions", "ExpirationMonthNumber", "CardMonthIsDropdown");

        executePossibleDropdown("CardYearInstructions", "ExpirationYearNumber", "CardYearIsDropdown");

        xpathClick(form.get("PlaceOrderButton"));
        System.out.println("Purchase complete\nLink: " + driver.getCurrentUrl());
        recordPurchase(url);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("FUCK.");
      System.out.println("Trying again...");
      buyShoe(url);
    }
  }

  /**
   * Fills in a form input on the webdriver based on xpath
   *
   * @param inputElement  The input element on the form
   * @param valueInputted The value going into the inputElement
   */
  private static void xpathFill(String inputElement, String valueInputted) {
    driver.findElement(By.xpath(inputElement)).sendKeys(valueInputted);
    System.out.println(valueInputted + " filled");
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
      je.executeScript(jsScrollFunction, element);
    }
    element.click();
    System.out.println(elementToClick + " clicked");
  }

  /**
   * Method made specifically for dropdown menus
   * <p>
   * The corresponding properties file of the website
   * contains a sequence of xpaths tailored for that
   * dropdown.
   * <p>
   * The method runs through each xpath,
   * however if the element is not in view,
   * a WebDriver exception will be caught and
   * it will scroll down until it's clickable
   *
   * @param instructionsProperty Sequence of xpaths for dropdown
   * @param innerTextToSelect    innerHTML to select (This is pulled from user.properties
   * @param isDropdownProperty   Later parsed as boolean to determine if a dropdown menu is present
   */
  private static void executePossibleDropdown(String instructionsProperty, String innerTextToSelect, String isDropdownProperty) {
    JavascriptExecutor je = (JavascriptExecutor) driver;
    WebElement element;
    // Dropdowns require an unpredictable amount of clicks
    // This will loop through each xpath in the corresponding
    // properties file and click it
    System.out.println("Handling " + instructionsProperty + " dropdown");
    if (Boolean.parseBoolean(form.get(isDropdownProperty))) {

      String[] xpathInstructions = form.get(instructionsProperty).split("&");
      for (String instruction : xpathInstructions) {
        // Concat innerTextToSelect with xpath method and try clicking on it
        element = driver.findElement(By.xpath(instruction.replace("REPLACE", user.get(innerTextToSelect)))); // idek what this does and i wrote it
        try {
          element.click();
          System.out.println(instructionsProperty + " clicked");
        } catch (WebDriverException wde) {
          // Occurs when the element must be scrolled into view before being clickable
          je.executeScript(jsScrollFunction, element);
          element.click();
          System.out.println(instructionsProperty + " clicked");
        }
      }
    } else {
      // If the form element is not a dropdown we can just simple click the element
      driver.findElement(By.xpath(instructionsProperty)).click();
    }
  }

  /**
   * After purchasing the shoe,
   * the url will be written to this file
   * to exit the while() loop in the buyShoe()
   * method. This will prevent duplicates
   *
   * @param url url of shoe
   */
  private static void recordPurchase(String url) {
    // Establish PrintWriter
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(purchaseTxt, true)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Write url to file
    out.println(url);
    System.out.println("\nRecord saved " + url);
    out.close();
  }

  /**
   * This method will exit the while loop
   *
   * @param url url of the shoe
   * @return whether the shoe url has been written to the file
   */
  private static Boolean notCopped(String url) {
    System.out.println("url = " + url);
    try {
      // Read in file and check for a match
      BufferedReader br = new BufferedReader(new FileReader(purchaseTxt));
      String inline;
      while ((inline = br.readLine()) != null) {
        System.out.println("inline = " + inline);

        if (inline.equals(url)) {
          // A match was found meaning the user already purcahses
          System.out.println("Shoe owned");
          return false;
        }
      }
      System.out.println("Shoe not owned");
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Checks if a reCaptcha exists on the webpage
   * if there is a captcha it will wait until the
   * user to solve it and user input
   */
  private static void captcha() {
    try {
      System.out.println("\nChecking for captcha...");
      if (!driver.findElements(By.xpath(form.get("Captcha"))).isEmpty()) {
        // Element exists
        System.out.println("Captcha found!");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nSolve the reCaptcha on the webpage" +
          "\nType anything in the console once completed");

        // Wait for user to finish captcha and provide user input
        if (br.readLine() != null) {
          System.out.println("Captcha solved!\nContinuing checkout...");
        }
      } else {
        System.out.println("\nNo reCaptcha found");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


