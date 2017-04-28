

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Anthony Vardaro on 4/11/2017.
 * http://www.adidas.com/us/nmd_cs1-primeknit-shoes/BA7209.html
 *
 * http://www.adidas.com/us/pureboost-shoes/BA8898.html
 *
 * http://www.adidas.com/us/nmd_xr1-primeknit-shoes/BA7215.html
 *
 * http://www.adidas.com/us/superstar-boost-shoes/BB0191.html
 */

public class Main {

  public static void main(String[] args) {
    // Establish ChromeDriver executable in properties
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\justa\\Downloads\\chromedriver_win32\\chromedriver.exe");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Enter link into console below");

      Purchaser.buyShoe(br.readLine());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
