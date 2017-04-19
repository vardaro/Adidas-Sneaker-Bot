import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import twitter4j.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by Anthony Vardaro on 4/11/2017.
 */

public class Main {
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
  // Shoe size is subject to change depending if shoe is True to Size (TTS)
  private static int ShoeSize = Integer.parseInt(user.get("ShoeSize"));

  public static void main(String[] args) {

    // Establish ChromeDriver executable in properties
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\justa\\Downloads\\chromedriver_win32\\chromedriver.exe");
    buyShoe("");
    BasicConfigurator.configure();
    Twitter twitter = getTwitter();

    try {
      StartListening("freshpepperonis", twitter, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Stores Twitter API access keys into Twitter object
   *
   * @return Twitter object containing API keys
   */
  private static Twitter getTwitter() {
    System.out.println("Getting Twitter properties...");
    Twitter twitter = new TwitterFactory().getSingleton();
    System.out.println("Twitter received");
    return twitter;
  }

  /**
   * Takes a status and prints relevant info about the tweet
   * to the console for users to see.
   *
   * @param s Tweet found
   */
  private static void printTweetInfo(Status s) {
    //System.out.println("\nNew @" + s.getUser().getScreenName() + " tweet found!");
    System.out.println("Date Tweeted: " + s.getCreatedAt());
    System.out.println("Tweet ID: " + s.getId());
    System.out.println("Body: {\n" + s.getText() + "\n}");

  }

  /**
   * Records tweet info into designated .txt file
   * of the corresponding twitter account.
   * <p>
   * This prevents a tweet from being found twice and
   * accidentally purchasing the same shoe twice by
   * reassigning the value of lastIDUsed long to the tweets ID.
   * <p>
   * This method will only be called after a purchase has been made.
   *
   * @param s Latest status from specified twitter account
   */
  private static void recordTweet(Status s) {
    // Try to create PrintWriter object
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(adidasalerts, true)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Write info of the tweet to the file
    out.println("tweedID=" + s.getId());
    out.println("tweetDate=" + s.getCreatedAt());
    out.println("Body={\n" + s.getText() + "\n}");
    out.println();
    out.close();
  }

  /**
   * Listens for new tweets from specified twitter user
   * <p>
   * If the user tweets a restock or release of new shoe,
   * it will travel to the provided link in the tweet to
   * purchase the shoe
   * <p>
   * TODO: Finish tweet listener and add support for shoe value logic
   *
   * @param username          Twitter users @
   * @param twitter           Twitter object
   * @param checkoutPermitted if true, will purchase the shoe, otherwise it just prints the tweet and moves on
   */
  private static void StartListening(final String username, Twitter twitter, final boolean checkoutPermitted) {
    try {
      User user = twitter.showUser(username);
      // Declaring TwitterStream object
      TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

      final StatusListener listener = new StatusListener() {
        //Declaring each method
        public void onStatus(Status status) {
          // Immediately filter out retweets and replies
          if (status.getUser().getScreenName().equals(username)) {
            // Print newly discovered tweet
            System.out.println("Here is a new tweet");
            printTweetInfo(status);

            if (checkoutPermitted) {
              String link = extractLink(status);

              recordTweet(status);

            }
            // Creating new instance of ChromeDriver()
            WebDriver driver = new ChromeDriver();

            // Visit website
            driver.get("http://google.com");
            WebElement element = driver.findElement(By.name("q"));
            element.sendKeys("Anthony Vardaro");
            element.submit();

          }
        }

        public void onDeletionNotice(StatusDeletionNotice sdn) {
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        public void onException(Exception e) {
          e.printStackTrace();
        }

        public void onScrubGeo(long userID, long upToStatusID) {
        }

        public void onStallWarning(StallWarning warning) {
        }
      };

      // Add listener to TwitterStream object
      // TwitterStream only streams tweets from specified user
      twitterStream.addListener(listener);
      FilterQuery query = new FilterQuery(user.getId());
      twitterStream.filter(query);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Restcok and release announcement always include a link
   * This method returns the link to the shoe
   *
   * @param s Status
   * @return link
   */
  public static String extractLink(Status s) {
    return s.getURLEntities()[0].getURL();
  }

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
