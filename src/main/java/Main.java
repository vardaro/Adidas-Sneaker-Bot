import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Scanner;

/**
 * Created by Anthony Vardaro on 4/11/2017.
 */

public class Main {

  public static void main(String[] args) {
    // Establish ChromeDriver executable in properties
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\justa\\Downloads\\chromedriver_win32\\chromedriver.exe");
    Purchaser.buyShoe("");
  }
}
